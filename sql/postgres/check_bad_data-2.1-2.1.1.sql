select * 
from ir_user.personal_file pf1
where exists( select * from ir_user.personal_file pf2
where pf2.user_id = pf1.user_id
and pf2.versioned_file_id = pf1.versioned_file_id
and pf2.personal_file_id != pf1.personal_file_id)