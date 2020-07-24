package com.leateck.gmp.backup.constant;

/**
 * <p>Title: BackupConstant</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-03   luyangqian  Created
 * </pre>
 */
public class BackupConstant {

    /** 本地连接 */
    public final static String CONNECT_LOCAL_TYPE_VAR = "0";

    /** ssh */
    public final static String DEFAULT_SSH_TYPE_VAR = "1";

    /** ftp */
    public final static String CONNECT_FTP_TYPE_VAR = "2";

    public final static String DEFAULT_SYS_TYPE = "0";

    public final static String DEFAULT_SORT = "22";

    /**
     * cron的方式
     */
    public final static String DEFAULT_CORN_EXPR_TYPE = "0";
    public final static String DEFAULT_CORN_EXPR_TYPE_JAVA = "1";

    /**
     * 源服务器
     */
    public final static String SERVER_TYPE_SOURCE_VAR = "0";

    /**
     * 目标服务器
     */
    public final static String SERVER_TYPE_TARGET_VAR = "1";

    public final static String BASE_FIELD_ROW_ID = "rowId";
    public final static String BASE_FIELD_CREATE_TIME = "createDate";
    public final static String BASE_FIELD_UPDATE_TIME = "updateDate";


    /**
     * 分页查询默认值
     */
    public final static int SEARCH_DEFAULT_PAGE_NUM = 1;
    public final static int SEARCH_DEFAULT_PAGE_SIZE = 10;

    public final static int DEFAULT_SAVE_DAY_NUM = 30;
}
