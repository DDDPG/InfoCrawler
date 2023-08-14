# Crawler-backend

## API
API Verification Specification: refer: https://juejin.cn/post/7249382407244087351#heading-18

API:
* login: `/api/v1/login`
* user: `/api/v1/users/info`
* loginCode: `/api/v1/login/code`
* table 
  * CrawlerInfoTable:  `/api/v1/resultInfoTable`
  * ManagementTable: `/api/v1/crawlerManagementTable`
  * UserTable: `/api/v1/userTable`


## Structure
### OuterSettings&Utils: `com.crawlerdemo.constant/utils`
Import from RuoYi project: https://gitee.com/y_project/RuoYi-Vue 

### Config: `com.crawlerdemo.webmagic.config`
   * `CrawlerManagementConfig`: init websites which need to crawl from `CrawlerManagementMap`
   * `CrawlerSettingConfig`: init global performance settings of crawler like thread number and max retry times
   * `MybatisPlusConfig`: MybatisPlus plugins and config

### Schedule: `com.crawlerdemo.schedule`
Integrate `xxl-job` framework as executor-name: `webmagic-executor` Bean mode.

### Security: `com.crawlerdemo.security`
SpringSecurity module, `JWT login` implementation and `BCrypt algorithm` password encoder, Redis cache

### Crawler: `com.crawlerdemo.webmagic`
The main package of the crawler, file structure refer: https://webmagic.io/docs/zh/posts/ch1-overview/architecture.html 

  * model: Info model for each website processed by crawler initially
  * pipeline: processing Info object extracted by crawler as model
  * CrawlerManager.class: Manager of all the crawlers for diff websites
  * Entity: DAO
  * Mail: process mail task

## How to use

### Local package and run
```bash
cd Crawler-backend
mvn clean package
java -jar target/Crawler-backend-0.0.1-SNAPSHOT.jar
java  
```

### Docker run
```bash
mvn clean package
docker build -t crawler-backend .
docker run -d -p 8080:8080 crawler-backend
```

## Maintenance Step

### New Website to crawl
1. Extract target website in new model class, extends the abstract class `com.crawlerdemo.webmagic.model.ResultInfoRepo` with following fields:
   * String `startUrl`: Entrance website of the crawler
   * Integer `id`: Primary key in database
   * String `sourceWebsite`: name of the source of info
   * String `url`: url of the current info
   * String `sourceUrl`: url of the source
   * String `area`: info belonging (Province, if null will be filled with "全国")
   * String `date`: published date of info, format `YYYY-MM-DD`
   * boolean `ifMatching`: to indicate if caught keyword in targeted keywords

> If want to add new extract field, please upgrade:
>   1. Java class `ResultInfoEntity` and `ResultInfoRepo`
>   2. `mapper.xml` in `Crawler-backend/src/main/resources/mappers/ResultInfoMapper.xml`

2. Upgrade `CrawlerManagementMap` in database or frontend page, including fields refer to `com.crawlerdemo.webmagic.Entity.crawler.CrawlerManagementEntity`
> In table columns, `contact` and `emails` are optional. 
> If `email` is empty, new info from this website would not send to anyone.
> If `crawlerKeywords` is empty, the crawler would not start.

3. Add new method for crawling new website in `com.crawlerdemo.webmagic.CrawlerManager`, then add this mothod and mapping keyword in method `crawl(keyword)`.

### New pipeline for after-crawling process
1. Add new pipeline class in `com.crawlerdemo.webmagic.pipeline` implements `us.codecraft.webmagic.pipeline.PageModelPipeline`
2. Upgrade crawl method for each websites in `com.crawlerdemo.webmagic.CrawlerManager`
eg:
``` Java
OOSpider.create(globalSite
        , companyInfoSQLPipeline, BiBinetRepo.class)
        .addUrl(BiBinetRepo.startUrl)
        .addPipeline(yourNewPipeline) // add new pipeline here
        .thread(threadNum).run();
```

### New schedule task
1. Add new method in `com.crawlerdemo.schedule.CrawlerXxlJob` and manage tasks in xxl-job-admin
2. Upgrade `Crawler-backend/src/main/resources/logback.xml` for better log

### New crawler performance setting
_**Corresponding frontend page has not finished**_

1. insert record in table `CrawlerSetting`
2. Ensure only one record with `status = 1`, which means this setting is applied
> If multiple settings with `status=1`, the first scanned one would be applied.

> If want to modify setting for each website, upgrade `com.crawlerdemo.webmagic.CrawlerManager`