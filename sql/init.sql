create database if not exists clothes_mall_database;

use clothes_mall_database;

create table sys_banner
(
    banner_url  varchar(255) null comment '轮播图',
    create_time timestamp    null,
    update_time timestamp    null,
    id          varchar(255) not null
        primary key
)
    comment '轮播图';

create table sys_discovery
(
    id           varchar(255) not null comment 'id'
        primary key,
    user_id      varchar(255) null comment '用户id',
    desc_text    text         null comment '文本描述',
    create_time  timestamp    null comment '创建时间',
    update_time  timestamp    null comment '更新时间',
    img          text         null comment '图片路径JSON形式',
    likes        int          null comment '点赞数量',
    like_users   text         null comment '点赞用户JSON',
    unread_likes text         null comment 'json形式的对象记录未读的赞',
    is_pass      int          null comment '审核是否通过 1 通过 0 未通过'
)
    comment '商品朋友圈有外键';

create table sys_discovery_comment
(
    id           varchar(255) not null
        primary key,
    create_time  timestamp    null,
    update_time  timestamp    null,
    comment_id   varchar(255) null comment '商品朋友圈ID/评论ID',
    content      text         null comment '评论内容',
    user_id      varchar(255) null comment '评论人',
    is_read      int          null comment '是否已读(1 已读 0 未读)',
    comment_type varchar(255) null comment '评论类型',
    discovery_id varchar(255) null comment '发现ID'
)
    comment '评论内容';

create index sys_discovery_comments_fk
    on sys_discovery_comment (comment_id);

create table sys_file
(
    id          varchar(255) not null
        primary key,
    path        varchar(255) null comment '保存路径',
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '修改时间',
    url         varchar(255) null comment '下载url'
)
    comment '文件';

create table sys_order
(
    id               varchar(255) not null
        primary key,
    create_time      timestamp    null,
    update_time      timestamp    null,
    state            varchar(255) null comment '订单状态',
    order_number     varchar(255) null comment '下单编号',
    user_id          varchar(255) null,
    delivery_address varchar(255) null comment '配送地址',
    product_sku      text         null comment 'json形式sku信息',
    product_detail   text         null comment '商品信息',
    is_evaluate      int          null comment '是否评价了 1 已评价 -1 未处理商品 0 未评价 ',
    sku_number       int          null comment 'sku数量',
    total_price      decimal      null comment '总价格',
    size             varchar(255) null comment '尺码'
)
    comment '订单列表有外键';

create table sys_product_comment
(
    id              varchar(255) not null comment 'id'
        primary key,
    star_rating     int          null comment '星级',
    user_id         varchar(255) null comment '用户ID',
    comment_content text         null comment '评论内容',
    comment_img_url varchar(255) null comment '评论图片json形式为多张图片',
    create_time     timestamp    null comment '创建时间',
    update_time     timestamp    null comment '更新时间',
    order_id        varchar(255) null comment '订单ID'
)
    comment '商品评论';

create table sys_product_details
(
    id               varchar(255) not null comment 'id'
        primary key,
    carousel         varchar(255) null comment '轮播图来自于sku',
    delivery_address varchar(255) null comment '发货地址',
    product_name     varchar(255) null,
    desc_urls        text         null comment '商品描述,图片都是为json',
    create_time      timestamp    null comment '创建时间',
    update_time      timestamp    null,
    product_type_ids varchar(255) null comment '商品类型ID'
)
    comment '商品详情有外键';

create table sys_product_sku
(
    id              varchar(255) not null comment 'id'
        primary key,
    product_id      varchar(255) not null comment '商品ID',
    price           decimal      null comment '价格',
    stock           int          null comment '库存',
    size            varchar(255) null comment '尺码',
    color           varchar(255) null comment '颜色',
    other_attribute text         null comment '其他属性',
    create_time     timestamp    null comment '创建时间',
    update_time     timestamp    null comment '更新时间',
    is_recommend    int          null comment '是否推荐，1 是 0 不是',
    special_price   decimal      null comment '特惠价格',
    is_special      int          null comment '是否为特惠 1 是 0 不是',
    constraint sys_product_sku_sys_product_details_id_fk
        foreign key (product_id) references sys_product_details (id)
            on update cascade on delete cascade
);

create table sys_product_type
(
    type        varchar(255) null comment '类型',
    id          varchar(255) not null comment 'id'
        primary key,
    update_time timestamp    null comment '更新时间',
    create_time timestamp    null comment '创建时间'
)
    comment '商品分类类型';

create table sys_shopping_cart
(
    id             varchar(255) not null
        primary key,
    create_time    timestamp    null,
    update_time    timestamp    null,
    product_sku_id varchar(255) null comment '商品id',
    number         int          null comment '数量',
    user_id        varchar(255) null comment '用户ID',
    size           varchar(255) null comment '尺寸大小'
)
    comment '购物车';

create table sys_user
(
    id               varchar(255) not null comment 'id'
        primary key,
    role             varchar(255) null comment '角色',
    pwd              varchar(255) null comment '密码',
    nickname         varchar(255) null comment '昵称',
    avatar           varchar(255) null,
    coordinate       varchar(255) null comment '坐标',
    create_time      timestamp    null,
    update_time      timestamp    null,
    shipping_address text         null comment '收货地址(可能是多个)',
    phone_number     varchar(255) null comment '手机号码',
    is_wechat_login  int          null comment '是否为微信登录(1 是 0 不是)',
    gender           int          null comment '0 是男 1是女',
    email            varchar(255) null comment '邮箱'
)
    comment '用户表';

create table sys_wallet
(
    id          varchar(255) not null
        primary key,
    balance     decimal      null comment '余额',
    user_id     varchar(255) null comment '用户ID',
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '更新时间'
)
    comment '钱包有外键';

create index sys_wallet_fk
    on sys_wallet (user_id);

create table sys_wallet_record
(
    expenditure decimal      null comment '支出',
    income      decimal      null comment '收入',
    user_id     varchar(255) null comment '用户ID',
    id          varchar(255) not null comment 'id'
        primary key,
    create_time timestamp    null comment '创建时间',
    update_time timestamp    null comment '修改时间'
)
    comment '钱包流水记录';

