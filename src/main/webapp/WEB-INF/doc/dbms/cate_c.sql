-- 1. 테이블 삭제
DROP TABLE cate;    -- 자식 삭제먼저
DROP TABLE categrp;     -- 그 다음 부모 삭제

-- 2. 테이블 생성
categrp     -- 부모 생성 먼저
cate        -- 그 다음 자식 생성

-- 3. CASCADE option을 이용한 테이블 삭제

/**********************************/
/* Table Name: 카테고리 그룹 */
/**********************************/
CREATE TABLE categrp(
		categrpno                     		NUMBER(10)		 NOT NULL,
		name                          		VARCHAR2(20)		 NULL ,
		seqno                         		NUMBER(10)		 NULL ,
		visible                       		CHAR(10)		 NULL ,
		rdate                         		DATE		 NULL 
);

COMMENT ON TABLE categrp is '카테고리 그룹';
COMMENT ON COLUMN categrp.categrpno is '카테고리 그룹 번호';
COMMENT ON COLUMN categrp.name is '카테고리 그룹 이름';
COMMENT ON COLUMN categrp.seqno is '출력 순서';
COMMENT ON COLUMN categrp.visible is '출력 모드';
COMMENT ON COLUMN categrp.rdate is '작성일';

DROP SEQUENCE categrp_seq;
CREATE SEQUENCE categrp_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지


/**********************************/
/* Table Name: 카테고리 */
/**********************************/
CREATE TABLE cate(
		cateno                        		NUMBER(10)		 NOT NULL,
		categrpno                     		NUMBER(10)		 NULL ,
		name                          		VARCHAR2(20)		 NOT NULL,
		seqno                         		NUMBER(10)		 NOT NULL,
		visible                       		CHAR(1)     DEFAULT 'Y'		 NOT NULL,
		rdate                         		DATE		 NOT NULL ,
		cnt                           		NUMBER(10)		NOT NULL,
        FOREIGN KEY (categrpno) REFERENCES categrp (categrpno);
);

COMMENT ON TABLE cate is '카테고리';
COMMENT ON COLUMN cate.cateno is '카테고리 번호';
COMMENT ON COLUMN cate.categrpno is '카테고리 그룹 번호';
COMMENT ON COLUMN cate.name is '카테고리 이름';
COMMENT ON COLUMN cate.seqno is '출력 순서';
COMMENT ON COLUMN cate.visible is '출력 모드';
COMMENT ON COLUMN cate.rdate is '등록일';
COMMENT ON COLUMN cate.cnt is '등록된 글 수';

DROP SEQUENCE cate_seq;
CREATE SEQUENCE cate_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지

ALTER TABLE categrp ADD CONSTRAINT IDX_categrp_PK PRIMARY KEY (categrpno);

ALTER TABLE cate ADD CONSTRAINT IDX_cate_PK PRIMARY KEY (cateno);
ALTER TABLE cate ADD CONSTRAINT IDX_cate_FK0 FOREIGN KEY (categrpno) REFERENCES categrp (categrpno);

-- 등록
INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 5, '가을', 1, 'Y', sysdate, 0);
오류 보고 -
SQL 오류: ORA-00904: "VISIVLE": invalid identifier


-- insert
INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '국내', 1, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '해외 여행', 2, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '개발', 3, 'Y', sysdate);

-- list
SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY categrpno ASC;

SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
ORDER BY cateno ASC;



UPDATE cate
SET categrpno=1, name='식당', seqno = 10, visible='N', cnt=0
WHERE cateno = 2;

DELETE cate
WHERE cateno=2;

commit;

-- 출력 순서 상향, 10 ▷ 1
UPDATE cate
SET seqno = seqno - 1
WHERE cateno=1;
 
-- 출력순서 하향, 1 ▷ 10
UPDATE cate
SET seqno = seqno + 1
WHERE cateno=1;

commit;

SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY seqno ASC;

-- 출력 모드의 변경
UPDATE cate
SET visible='Y'
WHERE cateno=1;

UPDATE cate
SET visible='N'
WHERE cateno=1;

-- 삭제하려면 레코드의 categrpno가 어디에서 쓰이는지 알려주어야함
SELECT COUNT(*) as cnt
FROM cate
WHERE categrpno=6;
-- 자식 테이블 FK가 1인 레코드 모두 삭제
DELETE FROM cate
WHERE categrpno=6;

DELETE FROM categrp
WHERE categrpno=6;
