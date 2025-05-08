select count(a.id) as fish_count
from fish_info a
join FISH_NAME_INFO b on a.fish_type = b.fish_type
where b.fish_name in ('BASS', 'SNAPPER')
