INSERT INTO voucher (voucher_no, company_id, period, voucher_type, total_amount, status, created_by)
VALUES
  ('VC-2024-001', 'COMP-A', '2024-01', 'EXPENSE',  1.005, 'POSTED', 'alice'),
  ('VC-2024-002', 'COMP-A', '2024-01', 'EXPENSE',  1.005, 'POSTED', 'alice'),
  ('VC-2024-003', 'COMP-A', '2024-01', 'EXPENSE',  1.005, 'POSTED', 'alice'),
  ('VC-2024-004', 'COMP-B', '2024-01', 'TRANSFER', 8000.00, 'DRAFT',  'bob'),
  ('VC-2024-005', 'COMP-A', '2024-02', 'EXPENSE',  3200.00, 'DRAFT',  'alice');

INSERT INTO voucher_entry (voucher_id, account_code, account_name, debit_amount, credit_amount, remark)
VALUES
  (1, '6601', '销售费用',    1.005,    0.00, '差旅费'),
  (1, '1002', '银行存款',    0.000, 1.005, '付款'),
  (2, '6601', '销售费用',    1.005,    0.00, '差旅费'),
  (2, '1002', '银行存款',    0.000, 1.005, '付款'),
  (3, '6601', '销售费用',    1.005,    0.00, '差旅费'),
  (3, '1002', '银行存款',    0.000, 1.005, '付款'),
  (4, '1002', '银行存款',    8000.00,    0.00, '内部划款'),
  (4, '1002', '银行存款',       0.00, 8000.00, '内部划款');
