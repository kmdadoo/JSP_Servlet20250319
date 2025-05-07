-- system 계정
alter session set "_ORACLE_SCRIPT"=true;

CREATE USER musthave IDENTIFIED BY 1234; 

-- 권한부여시 테이블 스페이스 권한을 추가로 부여해야한다. 
GRANT connect, resource, unlimited tablespace TO musthave;

-- 테이블 스페이스 조회 확인
SELECT tablespace_name, status, contents FROM dba_tablespaces;

-- 테이블 스페이스별 가용 공간 확인
SELECT tablespace_name, sum(bytes), max(bytes) from dba_free_space
    GROUP BY tablespace_name;
    
-- musthave 계정 사용하는 테이블 스페이스 확인
SELECT username, default_tablespace  FROM dba_users
    WHERE username in upper('musthave');

-- musthave 계정에 users 테이블 스페이스에 데이터를 입력할 수 있도록 
-- 5m 의 용량을 할당.
ALTER USER musthave QUOTA 5m ON users;

--- musthave 계정
SELECT * FROM tab;

DROP TABLE member;
DROP TABLE board;
DROP TABLE seq_board_num;

-- 회원 테이블 만들기
create table member(
    id varchar2(10) not null, 
    pass varchar2(10) not null,
    name varchar2(30) not null,
    regidate date default sysdate not null,
    primary key (id)
);

-- 더미 데이터 입력
insert into member (id, pass, name) values ('musthave', '1234', '머스트헤브');