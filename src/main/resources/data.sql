-- users テーブルにデータを挿入するクエリ
INSERT INTO users (name, password, email) VALUES ('サンプル太郎', 'himitu', 'tarou@example.com');

-- tasks テーブルにデータを挿入するクエリ
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 1, '見積もり書作成1', '2025-12-30', 0, '案件に適した見積もりを取る' ,'40' , '2025-12-30');
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 2, '買い物', '2025-12-31', 0, 'デパート散策' ,'60' , '2025-12-31');
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 1, '会議', '2025-12-29', 1, '' ,'40' , '2025-12-29');
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 2, '買い物', '2025-12-28', 1, '食料品の買い出し' ,'40' , '2025-12-28');
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 1, '見積もり書作成2', '2025-12-30', 2, '案件に適した見積もりを取る' ,'40' , '2025-12-30');


-- categories テーブルにデータを挿入するクエリ
INSERT INTO categories (category_id, category_name) VALUES (1, '仕事'), (2, '日常');