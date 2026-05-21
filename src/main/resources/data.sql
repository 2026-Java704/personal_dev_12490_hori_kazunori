-- users テーブルにデータを挿入するクエリ
INSERT INTO users (name, password) VALUES ('ロボット掃除機', 'himitu');

-- tasks テーブルにデータを挿入するクエリ
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 1, '見積もり', '2025-12-31', 0, '案件に適した見積もりを取る' ,'40' , '2025-12-30');
INSERT INTO tasks (user_id, category_id, title, closing_date, progress, memo,time, date) VALUES (1, 2, '見積もり', '2025-12-30', 2, '案件に適した見積もりを取る' ,'40' , '2025-12-30');

-- categories テーブルにデータを挿入するクエリ
INSERT INTO categories (category_id, category_name) VALUES (1, '仕事'), (2, '日常');