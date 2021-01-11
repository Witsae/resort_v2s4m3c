-- 1. ���̺� ����
DROP TABLE cate;    -- �ڽ� ��������
DROP TABLE categrp;     -- �� ���� �θ� ����

-- 2. ���̺� ����
categrp     -- �θ� ���� ����
cate        -- �� ���� �ڽ� ����

-- 3. CASCADE option�� �̿��� ���̺� ����

/**********************************/
/* Table Name: ī�װ� �׷� */
/**********************************/
CREATE TABLE categrp(
		categrpno                     		NUMBER(10)		 NOT NULL,
		name                          		VARCHAR2(20)		 NULL ,
		seqno                         		NUMBER(10)		 NULL ,
		visible                       		CHAR(10)		 NULL ,
		rdate                         		DATE		 NULL 
);

COMMENT ON TABLE categrp is 'ī�װ� �׷�';
COMMENT ON COLUMN categrp.categrpno is 'ī�װ� �׷� ��ȣ';
COMMENT ON COLUMN categrp.name is 'ī�װ� �׷� �̸�';
COMMENT ON COLUMN categrp.seqno is '��� ����';
COMMENT ON COLUMN categrp.visible is '��� ���';
COMMENT ON COLUMN categrp.rdate is '�ۼ���';

DROP SEQUENCE categrp_seq;
CREATE SEQUENCE categrp_seq
  START WITH 1              -- ���� ��ȣ
  INCREMENT BY 1          -- ������
  MAXVALUE 9999999999 -- �ִ밪: 9999999 --> NUMBER(7) ����
  CACHE 2                       -- 2���� �޸𸮿����� ���
  NOCYCLE;                     -- �ٽ� 1���� �����Ǵ� ���� ����


/**********************************/
/* Table Name: ī�װ� */
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

COMMENT ON TABLE cate is 'ī�װ�';
COMMENT ON COLUMN cate.cateno is 'ī�װ� ��ȣ';
COMMENT ON COLUMN cate.categrpno is 'ī�װ� �׷� ��ȣ';
COMMENT ON COLUMN cate.name is 'ī�װ� �̸�';
COMMENT ON COLUMN cate.seqno is '��� ����';
COMMENT ON COLUMN cate.visible is '��� ���';
COMMENT ON COLUMN cate.rdate is '�����';
COMMENT ON COLUMN cate.cnt is '��ϵ� �� ��';

DROP SEQUENCE cate_seq;
CREATE SEQUENCE cate_seq
  START WITH 1              -- ���� ��ȣ
  INCREMENT BY 1          -- ������
  MAXVALUE 9999999999 -- �ִ밪: 9999999 --> NUMBER(7) ����
  CACHE 2                       -- 2���� �޸𸮿����� ���
  NOCYCLE;                     -- �ٽ� 1���� �����Ǵ� ���� ����

ALTER TABLE categrp ADD CONSTRAINT IDX_categrp_PK PRIMARY KEY (categrpno);

ALTER TABLE cate ADD CONSTRAINT IDX_cate_PK PRIMARY KEY (cateno);
ALTER TABLE cate ADD CONSTRAINT IDX_cate_FK0 FOREIGN KEY (categrpno) REFERENCES categrp (categrpno);

-- ���
INSERT INTO cate(cateno, categrpno, name, seqno, visible, rdate, cnt)
VALUES(cate_seq.nextval, 5, '����', 1, 'Y', sysdate, 0);
���� ���� -
SQL ����: ORA-00904: "VISIVLE": invalid identifier


-- insert
INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '����', 1, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '�ؿ� ����', 2, 'Y', sysdate);

INSERT INTO categrp(categrpno, name, seqno, visible, rdate)
VALUES(categrp_seq.nextval, '����', 3, 'Y', sysdate);

-- list
SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY categrpno ASC;

SELECT cateno, categrpno, name, seqno, visible, rdate, cnt
FROM cate
ORDER BY cateno ASC;



UPDATE cate
SET categrpno=1, name='�Ĵ�', seqno = 10, visible='N', cnt=0
WHERE cateno = 2;

DELETE cate
WHERE cateno=2;

commit;

-- ��� ���� ����, 10 �� 1
UPDATE cate
SET seqno = seqno - 1
WHERE cateno=1;
 
-- ��¼��� ����, 1 �� 10
UPDATE cate
SET seqno = seqno + 1
WHERE cateno=1;

commit;

SELECT categrpno, name, seqno, visible, rdate
FROM categrp
ORDER BY seqno ASC;

-- ��� ����� ����
UPDATE cate
SET visible='Y'
WHERE cateno=1;

UPDATE cate
SET visible='N'
WHERE cateno=1;

-- �����Ϸ��� ���ڵ��� categrpno�� ��𿡼� ���̴��� �˷��־����
SELECT COUNT(*) as cnt
FROM cate
WHERE categrpno=6;
-- �ڽ� ���̺� FK�� 1�� ���ڵ� ��� ����
DELETE FROM cate
WHERE categrpno=6;

DELETE FROM categrp
WHERE categrpno=6;
