USE [Coupons]
GO

/* ==============================================
   Creates Coupons project DB
   ============================================== */


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO



/* ==============================================
   USER_PROFILE table
   ============================================== */

if OBJECT_ID ('USER_PROFILE') is not null
	DROP TABLE dbo.USER_PROFILE
GO

if OBJECT_ID ('USER_PROFILES') is not null
	DROP TABLE dbo.USER_PROFILES
GO

/*
CREATE TABLE dbo.USER_PROFILES
(
	USER_PROFILE_ID  	bigint IDENTITY(1,1)NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	USER_PROFILE_DESC	varchar(max)		NOT NULL,

	CONSTRAINT [PK_USER_PROFILES] PRIMARY KEY CLUSTERED 
	(
		USER_PROFILE_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE USER_PROFILES ADD CONSTRAINT DF_USER_PROFILES_SYS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE USER_PROFILES ADD CONSTRAINT DF_USER_PROFILES_SYS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

insert into USER_PROFILES ( USER_PROFILE_DESC, CREATED_BY_USER_ID, UPDATED_BY_USER_ID ) select 'Admin', 1, 1;
insert into USER_PROFILES ( USER_PROFILE_DESC, CREATED_BY_USER_ID, UPDATED_BY_USER_ID ) select 'Company', 1, 1 ;
insert into USER_PROFILES ( USER_PROFILE_DESC, CREATED_BY_USER_ID, UPDATED_BY_USER_ID ) select 'Customer', 1, 1;

*/

/* ==============================================
   USERS table
   ============================================== */

if OBJECT_ID ('USER') is not null
	DROP TABLE dbo.[USER]
GO


if OBJECT_ID ('USERS') is not null
	DROP TABLE dbo.USERS
GO

CREATE TABLE dbo.USERS
(
	USER_ID  			int					IDENTITY(1,1)NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	USER_PROFILE_ID  	int 				NOT NULL, /* 1 = Admin , 2 = Company, 3 = Customer */
	USER_NAME			varchar(max)		NOT NULL,
	LOGIN_NAME			varchar(100)		NOT NULL,
	LOGIN_PASSWORD		varchar(max)		NOT NULL,


	CONSTRAINT [PK_USERS] PRIMARY KEY CLUSTERED 
	(
		USER_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE USERS ADD CONSTRAINT DF_USERS_SYS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE USERS ADD CONSTRAINT DF_USERS_SYS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO


CREATE UNIQUE INDEX IDX_USERS_LOGIN_NAME ON USERS (LOGIN_NAME);  
GO



insert into USERS ( USER_PROFILE_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, USER_NAME, LOGIN_NAME, LOGIN_PASSWORD ) select '1', 1, 1 , 'USER 1', 'admin', '1234';

insert into USERS ( USER_PROFILE_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, USER_NAME, LOGIN_NAME, LOGIN_PASSWORD ) select '2', 1, 1 , 'USER 2', 'comp1' , '1234';
insert into USERS ( USER_PROFILE_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, USER_NAME, LOGIN_NAME, LOGIN_PASSWORD ) select '2', 1, 1 , 'USER 3', 'comp2' , '1234';

insert into USERS ( USER_PROFILE_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, USER_NAME, LOGIN_NAME, LOGIN_PASSWORD ) select '3', 1, 1 , 'USER 4', 'cust1' , '1234';
insert into USERS ( USER_PROFILE_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, USER_NAME, LOGIN_NAME, LOGIN_PASSWORD ) select '3', 1, 1 , 'USER 5', 'cust2' , '1234';


/* ==============================================
   COMPANY table
   ============================================== */

if OBJECT_ID ('COMPANY') is not null
	DROP TABLE dbo.COMPANY
GO

if OBJECT_ID ('COMPANYS') is not null
	DROP TABLE dbo.COMPANYS
GO


CREATE TABLE dbo.COMPANYS
(
	COMPANY_ID			bigint IDENTITY(1,1)NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	USER_ID				int					NOT NULL,
	COMPANY_NAME		varchar(900)		NOT NULL,
	--COMPANY_PASSWORD	varchar(max)		NOT NULL,
	COMPANY_EMAIL		varchar(max)		NOT NULL,


	CONSTRAINT [PK_COMPANYS] PRIMARY KEY CLUSTERED 
	(
		COMPANY_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE COMPANYS ADD CONSTRAINT DF_COMPANYS_SYS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE COMPANYS ADD CONSTRAINT DF_COMPANYS_SYS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

CREATE UNIQUE INDEX IDX_COMPANYS_COMPANY_NAME	ON COMPANYS (COMPANY_NAME);  
GO

CREATE UNIQUE INDEX IDX_COMPANYS_USER_ID		ON COMPANYS (USER_ID);  
GO

--insert into COMPANY ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, COMPANY_NAME, COMPANY_PASSWORD ) select 2, 1, 1, 'Company 1' , '1111';
--insert into COMPANY ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, COMPANY_NAME, COMPANY_PASSWORD ) select 3, 1, 1, 'Company 2' , '2222';

insert into COMPANYS ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, COMPANY_NAME, COMPANY_EMAIL ) select 2, 1, 1, 'Company 1' , 'comp1@gmail.com';
insert into COMPANYS ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, COMPANY_NAME, COMPANY_EMAIL ) select 3, 1, 1, 'Company 2' , 'comp2@gmail.com';



/* ==============================================
   CUSTOMER table
   ============================================== */

if OBJECT_ID ('CUSTOMER') is not null
	DROP TABLE dbo.CUSTOMER
GO

if OBJECT_ID ('CUSTOMERS') is not null
	DROP TABLE dbo.CUSTOMERS
GO

CREATE TABLE dbo.CUSTOMERS
(
	CUSTOMER_ID			bigint IDENTITY(1,1)NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	USER_ID				int					NOT NULL,
	CUSTOMER_NAME		varchar(900)		NOT NULL,
	--CUSTOMER_PASSWORD	varchar(max)		NOT NULL,
	--CUSTOMER_EMAIL		varchar(max)		 ,

	CONSTRAINT [PK_CUSTOMERS] PRIMARY KEY CLUSTERED 
	(
		CUSTOMER_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE CUSTOMERS ADD CONSTRAINT DF_CUSTOMERS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE CUSTOMERS ADD CONSTRAINT DF_CUSTOMERS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

CREATE UNIQUE INDEX IDX_CUSTOMERSS_CUSTOMER_NAME ON CUSTOMERS (CUSTOMER_NAME);  
GO


insert into CUSTOMERS ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, CUSTOMER_NAME  ) select 4, 1, 1, 'CUSTOMER 1'  ;
insert into CUSTOMERS ( USER_ID, CREATED_BY_USER_ID, UPDATED_BY_USER_ID, CUSTOMER_NAME  ) select 5, 1, 1, 'CUSTOMER 2'  ;
go


/* ==============================================
   COUPON_TYPE table
   ============================================== */

if OBJECT_ID ('COUPON_TYPE') is not null
	DROP TABLE dbo.COUPON_TYPE
GO


if OBJECT_ID ('COUPON_TYPES') is not null
	DROP TABLE dbo.COUPON_TYPES
GO

/*
CREATE TABLE dbo.COUPON_TYPES
(
	COUPON_TYPE_ID		bigint IDENTITY(1,1)NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	NAME				varchar(max)		NOT NULL,


	CONSTRAINT [PK_COUPON_TYPES] PRIMARY KEY CLUSTERED 
	(
		COUPON_TYPE_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE COUPON_TYPES ADD CONSTRAINT DF_COUPON_TYPES_SYS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE COUPON_TYPES ADD CONSTRAINT DF_COUPON_TYPES_SYS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

insert into COUPON_TYPES ( CREATED_BY_USER_ID, UPDATED_BY_USER_ID, NAME ) select 1,1,'Food coupon';
insert into COUPON_TYPES ( CREATED_BY_USER_ID, UPDATED_BY_USER_ID, NAME ) select 1,1,'Electricity coupon';
insert into COUPON_TYPES ( CREATED_BY_USER_ID, UPDATED_BY_USER_ID, NAME ) select 1,1,'Leisure coupon';
insert into COUPON_TYPES ( CREATED_BY_USER_ID, UPDATED_BY_USER_ID, NAME ) select 1,1,'Vacation coupon';
*/

/* ==============================================
   COUPON table
   ============================================== */

if OBJECT_ID ('COUPON') is not null
	DROP TABLE dbo.COUPON
GO

if OBJECT_ID ('COUPONS') is not null
	DROP TABLE dbo.COUPONS
GO

CREATE TABLE dbo.COUPONS
(
	COUPON_ID			bigint IDENTITY(1,1)	NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,

	COMPANY_ID			bigint				NOT NULL,
	COUPON_TITLE		varchar(900)		NOT NULL,
	COUPON_START_DATE	datetime			NOT NULL,
	COUPON_END_DATE		datetime			NOT NULL,
	COUPONS_IN_STOCK	int				    NOT NULL,
	COUPON_TYPE_ID		int					NOT NULL,
	COUPON_MESSAGE		varchar(max)		,
	COUPON_PRICE		float			    NOT NULL,
	IMAGE_FILE_NAME		varchar(max)		,


	CONSTRAINT [PK_COUPONS] PRIMARY KEY CLUSTERED 
	(
		COUPON_ID ASC
	)
) ON [PRIMARY]
GO

ALTER TABLE COUPONS ADD CONSTRAINT DF_COUPONS_SYS_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE COUPONS ADD CONSTRAINT DF_COUPONS_SYS_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

CREATE UNIQUE INDEX IDX_COUPONS_COUPON_TITLE		ON COUPONS (COUPON_TITLE);  
GO


/* ==============================================
   CUSTOMER_COUPON table
   ============================================== */

if OBJECT_ID ('CUSTOMER_COUPON') is not null
	DROP TABLE dbo.CUSTOMER_COUPON
GO

CREATE TABLE dbo.CUSTOMER_COUPON
(

	INTERNAL_ID			bigint IDENTITY(1,1)	NOT NULL,	

	CUSTOMER_ID			bigint				NOT NULL,
	COUPON_ID			bigint				NOT NULL,

	SYS_CREATION_DATE	datetime			not null,
	SYS_UPDATE_DATE		datetime			not null,
	CREATED_BY_USER_ID	int					not null,
	UPDATED_BY_USER_ID	int					not null,
	


	CONSTRAINT [PK_CUSTOMER_COUPON] PRIMARY KEY CLUSTERED 
	(
		 CUSTOMER_ID	ASC
		,COUPON_ID		ASC 
	)
) ON [PRIMARY]
GO

ALTER TABLE CUSTOMER_COUPON ADD CONSTRAINT DF_CUSTOMER_COUPON_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO

ALTER TABLE CUSTOMER_COUPON ADD CONSTRAINT DF_CUSTOMER_COUPON_UPDATE_DATE DEFAULT GETDATE() FOR SYS_UPDATE_DATE;
GO

/* ==============================================
   COMPANY_COUPON table
   ============================================== */

/*
if OBJECT_ID ('COMPANY_COUPON') is not null
	DROP TABLE dbo.COMPANY_COUPON
GO
*/



/*
CREATE TABLE dbo.COMPANY_COUPON
(
	INTERNAL_ID			int IDENTITY(1,1)	NOT NULL,	
	SYS_CREATION_DATE	datetime			not null,
	COMPANY_ID			int					NOT NULL,
	COUPON_ID			int					NOT NULL,


	CONSTRAINT [PK_COMPANY_COUPON] PRIMARY KEY CLUSTERED 
	(
		 COMPANY_ID		ASC
		,COUPON_ID		ASC 
	)
) ON [PRIMARY]
GO

ALTER TABLE COMPANY_COUPON ADD CONSTRAINT DF_COMPANY_COUPON_CREATION_DATE DEFAULT GETDATE() FOR SYS_CREATION_DATE;
GO
*/


/* ==============================================
   End of script
   ============================================== */
SET ANSI_PADDING OFF
GO


