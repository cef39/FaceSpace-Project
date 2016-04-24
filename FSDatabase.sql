PURGE RECYCLEBIN;
COMMIT;

DROP TABLE Profiles CASCADE CONSTRAINTS;
DROP TABLE Friends CASCADE CONSTRAINTS;
DROP TABLE Groups CASCADE CONSTRAINTS;
DROP TABLE Messages CASCADE CONSTRAINTS;
DROP TABLE UserGroups CASCADE CONSTRAINTS;

CREATE TABLE Profiles 
(
	user_ID number(10) PRIMARY KEY,
	fname varchar2(32),
	lname varchar2(32),
	email varchar2(32),
	birthday DATE,
	login TIMESTAMP
);

CREATE TABLE Friends
(
	user_ID number(10),
	friendID number(10),
	relationship number(1),
	establishedDate DATE,
	CONSTRAINT Friends_FK_Profiles FOREIGN KEY (user_ID) REFERENCES Profiles(user_ID),
	CONSTRAINT Friends_FK_Profiles2 FOREIGN KEY (friendID) REFERENCES Profiles(user_ID)
);

CREATE TABLE Groups
(
	group_ID number(10) PRIMARY KEY,
	group_name varchar2(32),
	group_description varchar2(150),
	group_limit number(10)
);

CREATE TABLE UserGroups
(
	user_ID number(10),
	group_ID number(10),
	CONSTRAINT UserGroups_FK_Profiles FOREIGN KEY (user_ID) REFERENCES Profiles(user_ID)
);

CREATE TABLE Messages
(
	sender_ID number(10),
	receiver_ID number(10),
	subject varchar2(50),
	msg_text varchar2(100),
	date_sent DATE,
	CONSTRAINT Messages_FK_Profiles FOREIGN KEY (sender_ID) REFERENCES Profiles(user_ID),
	CONSTRAINT Messages_FK_Profiles2 FOREIGN KEY (receiver_ID) REFERENCES Profiles(user_ID),
	CONSTRAINT Message_length CHECK (LENGTH(msg_text) <= 100)
);

