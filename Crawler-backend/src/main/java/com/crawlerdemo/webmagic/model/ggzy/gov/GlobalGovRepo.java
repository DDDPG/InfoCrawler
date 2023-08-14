package com.crawlerdemo.webmagic.model.ggzy.gov;

import com.crawlerdemo.webmagic.model.InfoType;
import com.crawlerdemo.webmagic.model.ResultInfoRepo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;

import java.util.List;

/**
 * @Package: com.example.crawlerdemo.webmagic.model.ggzy.gov
 * @description: Helper class for {@link GlobalGovPageRepo}.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GlobalGovRepo extends ResultInfoRepo<GlobalGovRepo> {

    private Integer id;

    private String sourceWebsite = "全国政府采购网";

    private String title;

    private String url;

    @ExtractByUrl("(.*)")
    private String sourceUrl;

    private String area;

    private String date;

    //The flag for matching the keywords. Used for the saveSQLOperation method.
    private boolean ifMatching;

    @Override
    public boolean ifMatchingFilter(List<String> filters) {
        for (String filter : filters) {
            if (this.title.contains(filter)) {
                this.ifMatching = true;
                return true;
            }
        }
        this.ifMatching = false;
        return false;
    }

    /**
     * This method is not used in this helper class.
     * @return null
     */
    @Override
    public List<GlobalGovRepo> formmatInfo() {
        return null;
    }


}
