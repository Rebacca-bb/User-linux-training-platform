-- ----------------------------
-- 课程表
-- ----------------------------
drop table if exists cou_courses;
create table cou_courses (
  course_id           bigint(20)      	not null auto_increment    comment '课程ID',
  course_name         varchar(30)     	not null                   comment '课程名称',
  course_info         varchar(100)    	not null                   comment '课程简介',
  course_typeID       bigint(20)       not null               			comment '课程类型id',
	
  course_source       varchar(255)     default ''                  comment '课程资源（URL？）',
	
  status            char(1)         default '0'                comment '课程状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',

  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (course_id)
) engine=innodb comment = '课程表';


-- ----------------------------
-- 初始化-课程表
-- ----------------------------
insert into cou_courses values('1', 'C++程序设计',  '这门课可以带你了解C++高级语言的设计', '1' ,'mooc.com', '0' ,'0', 'admin', sysdate(), '', null, '');
insert into cou_courses values('2', 'Java程序设计',    '这门课可以带你了解java高级语言的设计', '1' ,'mooc.com',  '0' ,'0', 'admin', sysdate(), '', null, '');
insert into cou_courses values('3', 'Django设计',    '这门课可以带你了解django的设计', '2' ,'mooc.com',  '0' ,'0', 'admin', sysdate(), '', null, '');

select * from cou_courses;