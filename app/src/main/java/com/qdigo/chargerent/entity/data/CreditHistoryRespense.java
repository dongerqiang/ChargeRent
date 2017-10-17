package com.qdigo.chargerent.entity.data;


import com.qdigo.chargerent.entity.net.responseBean.BaseResponse;

import java.util.List;


/**
 * Created by jpj on 2017-02-27.
 */

public class CreditHistoryRespense extends BaseResponse {

    /**
     * statusCode : 200
     * message : 成功返回信用积分历史
     * data : [{"scoreChange":1,"eventTime":"2017-02-23T07:04:45.000+0000","event":"正常骑行"}]
     */

    
    public List<CreditInfo> data;

}
