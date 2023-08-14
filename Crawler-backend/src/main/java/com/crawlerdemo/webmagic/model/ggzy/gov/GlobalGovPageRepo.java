package com.crawlerdemo.webmagic.model.ggzy.gov;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class GlobalGovPageRepo extends ResultInfoRepo<GlobalGovRepo> implements AfterExtractor, PageProcessor {

    public static String startUrl;

    static {
        //Refresh the startUrl every day
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        startUrl = "http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?" +
                "TIMEBEGIN_SHOW=" + yesterday.format(dateFormatter) +
                "&TIMEEND_SHOW=" + today.format(dateFormatter) +
                "&TIMEBEGIN=" + yesterday.format(dateFormatter) +
                "&TIMEEND=" + today.format(dateFormatter) +
                "&SOURCE_TYPE=1&DEAL_TIME=02" +
                "&DEAL_CLASSIFY=00" +
                "&DEAL_STAGE=0000&DEAL_PROVINCE=0" +
                "&DEAL_CITY=0" +
                "&DEAL_PLATFORM=0" +
                "&BID_PLATFORM=0" +
                "&DEAL_TRADE=0" +
                "&isShowAll=1" +
                "&PAGENUMBER=1" +
                "&FINDTXT=";
    }


    //List of single type info objects
    private List<GlobalGovRepo> globalGovRepoList = new ArrayList<>();

    @ExtractBy("/html/body/text()")
    private String textRaw;

    private JSONArray jsonArray;

    private String sourceWebsite = "政府采购网";

    private boolean ifMatching;

    @ExtractByUrl("(.*)")
    private String url;

    @Override
    public void afterProcess(Page page) {
        this.setJsonArray(JSONObject.parseObject(this.getTextRaw()).getJSONArray("data"));

        //Each item in the jsonArray is parsed into an GlobalGovRepo object
        for (int i = 0; i < this.getJsonArray().size(); i++) {
            JSONObject jsonObject = this.getJsonArray().getJSONObject(i);
            GlobalGovRepo globalGovRepo = new GlobalGovRepo();

            //JSON to GlobalGovRepo
            globalGovRepo.setTitle(jsonObject.getString("titleShow"));
            globalGovRepo.setUrl(jsonObject.getString("url"));
            globalGovRepo.setSourceUrl(this.getUrl());
            globalGovRepo.setArea(jsonObject.getString("districtShow"));
            globalGovRepo.setDate(jsonObject.getString("timeShow"));

            this.getGlobalGovRepoList().add(globalGovRepo);
        }

        //get the next page
        if (this.getJsonArray().size() > 0) {
            //get the current page number and total page number from the response in JSON format
            int currentPage = JSONObject.parseObject(this.getTextRaw()).getInteger("currentpage");
            int totalPage = JSONObject.parseObject(this.getTextRaw()).getInteger("ttlpage");

            //if has next page, add the next page request to the page
            if (currentPage < totalPage) {
                Request request = new Request();
                request.setUrl(this.url.substring(0, this.url.indexOf("PAGENUMBER=") + 11) +
                        (currentPage + 1) +
                        this.url.substring(this.url.indexOf("&FINDTXT=")));
                page.addTargetRequest(request);
            }

        }
    }

    @Override
    public void process(Page page) {
        this.textRaw = page.getHtml().xpath("/html/body/text()").toString();
    }


    @Override
    public boolean ifMatchingFilter(List<String> filters) {

        //if any of the oilGovRepo in the list matches the filter, return true
        //Simultaneously, each oilGovRepo refresh its `ifMatchingFilter` field for further save operation
        for (ResultInfoRepo oilGovRepo : this.getGlobalGovRepoList()) {
            this.ifMatching = this.ifMatching || oilGovRepo.ifMatchingFilter(filters);
        }

        return this.ifMatching;
    }

    @Override
    public List<GlobalGovRepo> formmatInfo() {
        return this.getGlobalGovRepoList();
    }

}
