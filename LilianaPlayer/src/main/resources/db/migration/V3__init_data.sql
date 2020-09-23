INSERT INTO `user` (`id`, `username`, `name`, `password`) VALUES ('1', 'admin', 'Admin', '{bcrypt}$2a$10$4TENNUAwIX4uG8GX2UWPBOuVSB3mBIlMcSVzAePDI/DHieJKQ0P7O');
INSERT INTO `user` (`id`, `username`, `name`, `password`) VALUES ('2', 'att', 'Tạ Anh Tú', '{bcrypt}$2a$10$4TENNUAwIX4uG8GX2UWPBOuVSB3mBIlMcSVzAePDI/DHieJKQ0P7O');

INSERT INTO `role` (`id`, `name`) VALUES ('1', 'ADMIN');
INSERT INTO `role` (`id`, `name`) VALUES ('2', 'USER');

INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES ('1', '1', '1');
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES ('2', '1', '2');
INSERT INTO `user_role` (`id`, `user_id`, `role_id`) VALUES ('3', '2', '2');
