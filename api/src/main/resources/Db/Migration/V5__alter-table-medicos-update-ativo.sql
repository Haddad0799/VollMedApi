UPDATE medicos
SET ativo = (CASE WHEN status = 'ATIVO' THEN true ELSE false END);
