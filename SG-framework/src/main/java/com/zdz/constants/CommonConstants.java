package com.zdz.constants;

public class CommonConstants {
    //文章发布状态
    public static final int ARTICLE_STATUS_PUBLISHED = 0;
    //文章草稿状态
    public static final int ARTICLE_STATUS_DRAFT = 1;
    //分页首页
    public static final int FIRST_PAGE = 1;
    //分页大小
    public static final int PAGE_SIZE = 10;

    //分类状态，0正常,1禁用
    public static final String CATEGORY_STATUS_NORMAL = "0";
    public static final String CATEGORY_STATUS_FORBIDDEN = "1";

    //友链状态
    public static final String LINK_STATUS_PASSED = "0";
    public static final String LINK_STATUS_FAILED = "1";
    public static final String LINK_STATUS_UNREVIEWED = "2";

    //评论类型，0文章评论，1友链评论
    public static final String COMMENT_TYPE_ARTICLE = "0";
    public static final String COMMENT_TYPE_LINK = "1";

    //根评论
    public static final Long COMMENT_ROOT = -1L;

    //菜单类型（M目录 C菜单 F按钮）
    public static final String MENU_TYPE_M = "M";
    public static final String MENU_TYPE_C = "C";
    public static final String MENU_TYPE_F = "F";

    //菜单状态（0正常 1停用）
    public static final String MENU_STATUS_NORMAL = "0";
    public static final String MENU_STATUS_DISABLED = "1";

    //用户类型
    public static final String USER_TYPE_ORDINARY = "0";
    public static final String USER_TYPE_ADMIN = "1";
    public static final Long SUPER_ADMIN = 1L;

    //角色状态（0正常 1停用）
    public static final String ROLE_STATUS_NORMAL = "0";
    public static final String ROLE_STATUS_DISABLED = "1";

    public static final String CATEGORY_EXPORT_FILE = "category";
}
