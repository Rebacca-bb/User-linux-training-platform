-- 创建"用户-容器-映射端口"表格

drop table if exists user_docker;
create table user_docker (
  id           				int(11)          	not null auto_increment       comment '表格中排序的id',
  user_name         	varchar(50)      	default ''                    comment '用户名称',
  container_id        varchar(255)     default ''                    comment '容器id',
	mapping_port        int(11)     			default 0                    comment '映射端口号',
	status            char(1)         default '0'                comment '容器状态（0正常 1停用）',
  primary key (id)
) engine=innodb auto_increment=1 default charset=utf8 comment = '用户-容器-映射端口信息表';