alter table story_backup
add column if not exists locale varchar(5) default 'ru_RU'