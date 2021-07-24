create table oauth_client_details
(
	client_id varchar(256) null,
	resource_ids varchar(256) null,
	client_secret varchar(256) null,
	scope varchar(256) null,
	authorized_grant_types varchar(256) null,
	web_server_redirect_uri varchar(256) null,
	authorities varchar(256) null,
	access_token_validity integer null,
	refresh_token_validity integer null,
	additional_information varchar(4096) null,
	autoapprove varchar(256) null,
	id varchar(20),
	constraint oauth_client_details_pk
	primary key (id)
);

create table oauth_access_token
(
	token_id varchar(256) null,
    authentication_id varchar(128),
    user_name varchar(256) null,
    client_id varchar(256) null,
    refresh_token varchar(256) null,
    token blob null,
    authentication blob null,
    constraint oauth_access_token_pk
    primary key (authentication_id)
);