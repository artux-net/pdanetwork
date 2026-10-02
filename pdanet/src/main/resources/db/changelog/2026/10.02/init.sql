alter table pda_user add column if not exists google_play_player_id varchar(255);

do $$
begin
    if not exists (
        select 1 from pg_constraint where conname = 'uq_pda_user_google_play_player_id'
    ) then
        alter table pda_user add constraint uq_pda_user_google_play_player_id unique (google_play_player_id);
    end if;
end $$;
