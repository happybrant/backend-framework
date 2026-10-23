-- ============================================================
-- 框架基础表 DDL（MySQL 8）
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinic DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE clinic;

-- -------------------- 框架：用户 / 角色 / 资源 --------------------
CREATE TABLE IF NOT EXISTS security_user
(
    id          VARCHAR(36) PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL COMMENT '登录名',
    password    VARCHAR(128) NOT NULL COMMENT '密码',
    avatar      VARCHAR(255) COMMENT '头像，图片URL',
    real_name   VARCHAR(50) COMMENT '真实姓名',
    email       VARCHAR(50) COMMENT '邮箱',
    remark      VARCHAR(2000) COMMENT '备注',
    status      VARCHAR(10) DEFAULT 'active' COMMENT '状态',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户表';

CREATE TABLE IF NOT EXISTS security_role
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL COMMENT '角色名',
    code        VARCHAR(50) NOT NULL COMMENT '角色编码',
    status      VARCHAR(10) DEFAULT 'active' COMMENT '状态',
    remark      VARCHAR(2000) COMMENT '备注',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色表';

CREATE TABLE IF NOT EXISTS security_resource
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL COMMENT '资源名',
    code        VARCHAR(50) NOT NULL COMMENT '资源编码',
    parent_id   VARCHAR(36) DEFAULT '0' COMMENT '父资源ID',
    type        VARCHAR(50) COMMENT '资源类型',
    status      VARCHAR(50) DEFAULT 'active' COMMENT '状态',
    sort        INT         DEFAULT 0 COMMENT '排序',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '资源表';

CREATE TABLE IF NOT EXISTS security_authorization
(
    id          VARCHAR(36) PRIMARY KEY,
    role_id     VARCHAR(36) NOT NULL COMMENT '角色ID',
    resource_id VARCHAR(36) NOT NULL COMMENT '资源ID',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '授权表';

CREATE TABLE IF NOT EXISTS security_user_role_rel
(
    id          VARCHAR(36) PRIMARY KEY,
    user_id     VARCHAR(36) NOT NULL,
    role_id     VARCHAR(36) NOT NULL COMMENT '角色ID',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户角色关联表';

CREATE TABLE IF NOT EXISTS security_log
(
    id          VARCHAR(36) PRIMARY KEY,
    content     VARCHAR(2000) COMMENT '操作内容简述',
    account     VARCHAR(100) COMMENT '操作用户账号',
    module      VARCHAR(100) COMMENT '操作模块',
    request_ip  VARCHAR(50) COMMENT '请求IP',
    start_time  DATETIME COMMENT '请求开始时间',
    end_time    DATETIME COMMENT '请求结束时间',
    cost        INT COMMENT '耗时',
    agent       VARCHAR(200) COMMENT '请求客户端',
    result_flag VARCHAR(10) DEFAULT 'success' COMMENT '返回结果标志',
    error_msg   TEXT COMMENT '错误信息',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '日志表';

CREATE TABLE IF NOT EXISTS utility_dict
(
    id          VARCHAR(36) PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    name        VARCHAR(100) NOT NULL COMMENT '字典名',
    parent_id   VARCHAR(36) DEFAULT '0' COMMENT '父字典ID',
    group_code  VARCHAR(50)  NOT NULL COMMENT '字典组编码',
    status      VARCHAR(50) DEFAULT 'active' COMMENT '状态',
    description VARCHAR(500) COMMENT '描述',
    sort        INT         DEFAULT 0 COMMENT '排序',
    create_user VARCHAR(36) COMMENT '创建用户',
    create_time DATETIME COMMENT '创建时间',
    update_user VARCHAR(36) COMMENT '更新用户',
    update_time DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '字典表';


CREATE TABLE IF NOT EXISTS utility_attachment
(
    id           VARCHAR(36) PRIMARY KEY,
    name         VARCHAR(200) COMMENT '文件名',
    origin_name  VARCHAR(200) COMMENT '原始文件名',
    storage_mode VARCHAR(20) COMMENT '文件存储模式',
    file_size    BIGINT COMMENT '文件大小，默认KB',
    full_path    VARCHAR(500) COMMENT '附件绝对路径',
    create_user  VARCHAR(36) COMMENT '创建用户',
    create_time  DATETIME COMMENT '创建时间',
    update_user  VARCHAR(36) COMMENT '更新用户',
    update_time  DATETIME COMMENT '更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '附件表';