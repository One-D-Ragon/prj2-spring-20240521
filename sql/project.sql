USE prj2;

# 게시물 테이블 생성
CREATE TABLE board
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    title    VARCHAR(100)  NOT NULL,
    content  VARCHAR(1000) NOT NULL,
    writer   VARCHAR(100)  NOT NULL,
    inserted DATETIME      NOT NULL DEFAULT NOW()
);

SELECT *
FROM board
ORDER BY id DESC;


# member table 만들기
CREATE TABLE member
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    nick_name VARCHAR(100) NOT NULL UNIQUE,
    inserted  DATETIME     NOT NULL DEFAULT NOW()
);

SELECT *
FROM member
ORDER BY id DESC;

# board 테이블 수정
# writer column 지우기
# member_id column reference member(id)

ALTER TABLE board
    DROP COLUMN writer;
DESC board;
ALTER TABLE board
    ADD COLUMN member_id INT REFERENCES member (id) AFTER content;
UPDATE board
SET member_id = (SELECT id FROM member ORDER BY id DESC LIMIT 1)
WHERE id > 0;
ALTER TABLE board
    MODIFY COLUMN member_id INT NOT NULL;
SELECT *
FROM board
ORDER BY id DESC;

SELECT *
FROM member
WHERE email = 'asd@asd';

DELETE
FROM board
WHERE member_id = 3;
DELETE
FROM member
WHERE email = 'asd@asd';

# 권한 테이블
CREATE TABLE authority
(
    member_id INT         NOT NULL REFERENCES member (id),
    name      VARCHAR(20) NOT NULL,
    PRIMARY KEY (member_id, name)
);

INSERT INTO authority (member_id, name)
VALUES (3, 'admin');

# 게시물 여러개 입력
INSERT INTO board
    (title, content, member_id)
SELECT title, content, member_id
FROM board;

SELECT *
FROM member;
UPDATE member
SET nick_name = 'abcd'
WHERE id = 13;
UPDATE member
SET nick_name = 'efgh'
WHERE id = 17;

UPDATE board
SET member_id = 13
WHERE id % 2 = 0;
UPDATE board
SET member_id = 17
WHERE id % 2 = 1;

UPDATE board
SET title   = 'abc def',
    content = 'ghi jkl'
WHERE id % 3 = 0;
UPDATE board
SET title   = 'mno pqr',
    content = 'stu vwx'
WHERE id % 3 = 1;
UPDATE board
SET title   = 'yz1 234',
    content = '567 890'
WHERE id % 3 = 2;