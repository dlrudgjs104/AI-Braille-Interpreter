상위글

CREATE TABLE `article` (
  `text` varchar(500) DEFAULT NULL,
  `ArticleId` int NOT NULL,
  `userId` int DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `recommandcount` int DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`ArticleId`),
  KEY `fk_userid3` (`userId`),
  CONSTRAINT `fk_userId` FOREIGN KEY (`userId`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `bookmark` (
  `categoryID` int DEFAULT NULL,
  `userID` int DEFAULT NULL,
  KEY `fk_categoryid` (`categoryID`),
  KEY `fk_userid2` (`userID`),
  CONSTRAINT `fk_categoryid` FOREIGN KEY (`categoryID`) REFERENCES `category` (`categoryID`),
  CONSTRAINT `fk_user` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `category` (
  `categoryID` int DEFAULT NULL,
  `categoryName` varchar(50) DEFAULT NULL,
  UNIQUE KEY `pk_categoryid` (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `permission` (
  `grade` int DEFAULT NULL,
  `permissionID` int NOT NULL,
  `permissionDetail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `post` (
  `articleid` int NOT NULL,
  `posttitle` varchar(50) DEFAULT NULL,
  `categoryid` int DEFAULT NULL,
  `replycount` int DEFAULT NULL,
  `viewcount` int DEFAULT NULL,
  PRIMARY KEY (`articleid`),
  KEY `fk_categoryid2` (`categoryid`),
  CONSTRAINT `fk_articleid` FOREIGN KEY (`articleid`) REFERENCES `article` (`ArticleId`),
  CONSTRAINT `fk_categoryid2` FOREIGN KEY (`categoryid`) REFERENCES `category` (`categoryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `recommandArticleList` (
  `userId` int DEFAULT NULL,
  `ArticleId` int DEFAULT NULL,
  KEY `fk_userid4` (`userId`),
  KEY `fk_articleid3` (`ArticleId`),
  CONSTRAINT `fk_articleid3` FOREIGN KEY (`ArticleId`) REFERENCES `article` (`ArticleId`),
  CONSTRAINT `fk_recommand` FOREIGN KEY (`userId`) REFERENCES `user` (`userID`),
  CONSTRAINT `fk_userid4` FOREIGN KEY (`userId`) REFERENCES `user` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


CREATE TABLE `user` (
  `userID` int NOT NULL,
  `Password` int DEFAULT NULL,
  `nickname` varchar(30) NOT NULL,
  `grade` int DEFAULT NULL,
  PRIMARY KEY (`userID`),
  KEY `fk_grade` (`grade`),
  CONSTRAINT `fk_grade` FOREIGN KEY (`grade`) REFERENCES `permission` (`permissionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


INSERT INTO article(articleid,userid,time,text) values(6,3,now(),'커피를 즐겨 마시면 체중 증가 위험이 줄어든다는 연구 결과가 나왔다. 대박');

INSERT INTO article(articleid,userid,time,text) values(1,1,now(),’《삼국유사》에는 선덕여왕이 앞날을 미리 알아낸 세 가지 일에 관한 이야기가 적혀 있습니다. 선덕여왕은 모란꽃 그림을 보고 향기가 없다는 것을 알았고, 백제의 적군이 숨어 들어온 일을 밝혔으며, 자신이 죽을 날을 미리 알았다고 했습니다. 이것으로 선덕여왕이 명민하고 슬기로웠다는 사실을 전하고 있습니다. 그러면 《삼국사기》에서는 선덕여왕에 대해 어떻게 기록하고 있는지 살펴봅시다.
[네이버 지식백과] 과거와 현재의 대화 - 역사란 무엇인가 (고교생이 알아야 할 한국사 스페셜, 2009. 2. 5., 김아네스, 최선혜)');

INSERT INTO article(articleid,userid,time,text) values(3,2,now(),’포르쉐코리아 (대표 홀가 게어만)가 더 강력한 드라이브 시스템을 갖춘 럭셔리 스포츠 세단 3세대 신형 파나메라를 국내 공식 출시한다고 밝혔다. 국내에는 파나메라4와 파나메라 터보 E-하이브리드를 시작으로 연내 파나메라 4E-하이브리드도 출시할 계획이다.');

INSERT INTO article(articleid,userid,time,text) values(7,1,now(),’안녕하세요 Team CSL 입니다 환절기에 미세먼지까지.. 팀원들 모두 건강을 유의해야 할 계절입니다!');

INSERT INTO article(articleid,userid,time,text) values(9,2,now(),’이정후는 8일(이하 한국시간) 미국 캘리포니아주 샌프란시스코 오라클 파크에서 열린 2024시즌 메이저리그 샌디에이고 파드리스와의 경기에 1번 중견수로 선발출전해 4타수 1안타 1득점을 기록했다. 샌프란시스코는 3-2 짜릿한 역전승을 거두고 샌디에이고와의 3연전을 위닝시리즈로 장식했다. ');

insert user
values(1, 1234, 'user', 3);

insert user
values(2, 4567, 'user2', 4);

insert user
values(3, 12345, 'user3', 2);

insert user
values(4, 1454, 'user4', 1);

insert user
values(5, 12234, 'user5', 5);

Insert category
values(1, ‘history’);

Insert category
values(2, ‘automobile’);

Insert category
values(3, ‘health’);

Insert category
values(4, ‘game’);

Insert category
values(5, ‘sports’);

INSERT INTO post(aritcleid,posttitle,categoryid,viewcount,replycount) values(1, '역사란 무엇인가', 1, 3 ,1);
INSERT INTO post(aritcleid,posttitle,categoryid,viewcount,replycount) values(3, '진짜 진짜 스포츠카예요. 이게 되네? 말이 안되는 차. 3세대 파나메라 터보 e하이브리드. 가', 3, 5 ,1);
INSERT INTO post(aritcleid,posttitle,categoryid,viewcount,replycount) values(5, '이 음료 즐겨 마시면 체중 증가 위험 감소한다', 5, 6 ,1);
INSERT INTO post(aritcleid,posttitle,categoryid,viewcount,replycount) values(7, '주사 맞을 시간이야! -LOL 간호사 아칼리 코스프레', 7, 7 ,1);
INSERT INTO post(aritcleid,posttitle,categoryid,viewcount,replycount) values(9, '169km 담장 직격 타구를 잡으라고? 이정후, 기대타율 .950 타구 놓치자 美매체 날선 질문 [오!쎈 샌프란시스코]', 9, 8 ,1);


INSERT INTO recommandArticleList VALUES(1, 1);
INSERT INTO recommandArticleList VALUES(2, 3);
INSERT INTO recommandArticleList VALUES(3, 5);
INSERT INTO recommandArticleList VALUES(4, 7);
INSERT INTO recommandArticleList VALUES(4, 9);

INSERT INTO bookmark VALUES(1, 5);
INSERT INTO bookmark VALUES(2, 2);
INSERT INTO bookmark VALUES(3, 3);
INSERT INTO bookmark VALUES(4, 1);
INSERT INTO bookmark VALUES(4, 2);

INSERT INTO article(articleid,userid,time,text) values(2,5,now(),'아하');
INSERT INTO article(articleid,userid,time,text) values(4,4,now(),'말도 안돼 ');
INSERT INTO article(articleid,userid,time,text) values(6,1,now(),'대박');
INSERT INTO article(articleid,userid,time,text) values(8,2,now(),'와우 ');
INSERT INTO article(articleid,userid,time,text) values(10,3,now(),'수고하셨습니다  ');

INSERT INTO reply(aritcleid,replyplaceid) values(2, 1);
INSERT INTO reply(articleid,replyplaceid)values(4, 3);
INSERT INTO reply(articleid,replyplaceid) values(6, 5);
INSERT INTO reply(articleid,replyplaceid) values(8, 7);
INSERT INTO reply(articleid, replyplaceid) values(10,9);