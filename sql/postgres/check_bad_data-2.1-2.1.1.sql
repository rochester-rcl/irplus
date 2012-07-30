-- Find bad file shares

select * 
from ir_user.personal_file pf1
where exists( select * from ir_user.personal_file pf2
where pf2.user_id = pf1.user_id
and pf2.versioned_file_id = pf1.versioned_file_id
and pf2.personal_file_id != pf1.personal_file_id)


-- Find bad emails

select *
from ir_user.user_email email1
where exists( select * from ir_user.user_email email2
where email2.user_email_id != email1.user_email_id
and lower(email2.email) = lower(email1.email));