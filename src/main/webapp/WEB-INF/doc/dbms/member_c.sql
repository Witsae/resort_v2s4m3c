/**********************************/
/* Table Name: 회원 */
/**********************************/
CREATE TABLE member(
        memberno                              NUMBER(10)         NOT NULL         PRIMARY KEY
);

COMMENT ON TABLE member is '회원';
COMMENT ON COLUMN member.memberno is '회원 번호';

DROP SEQUENCE member_seq;
CREATE SEQUENCE member_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지
  
INSERT INTO member(memberno)
VALUES(member_seq.nextval);

SELECT * FROM member;
  MEMBERNO
----------
         1
         
    SELECT COUNT(id) as cnt
    FROM member
    WHERE id='user1';
    
COMMIT;

-- 로그인
SELECT COUNT(*) as cnt
FROM member
WHERE id='user1' AND passwd='1234';
 
 cnt
 ---
   1

-- id를 이용한 회원 정보 조회
SELECT memberno, id, passwd, mname, tel, zipcode, address1, address2, mdate
FROM member
WHERE id = 'user1';