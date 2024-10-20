INSERT INTO Course (name, code, instructorId, description, status, inactivationDate, createdAt)
VALUES
('Curso de Java Avançado', 'CDJA-P', 1, 'Curso avançado sobre programação em Java', 'ACTIVE', NULL, NOW()),
('Curso de Spring Boot', 'CDSB-A', 2, 'Curso completo de Spring Boot para desenvolvimento de APIs', 'ACTIVE', NULL, NOW()),
('Curso de Microservices', 'CDM-D', 3, 'Curso de microservices com Spring Cloud e Docker', 'ACTIVE', NULL, NOW()),
('Curso de Testes com JUnit', 'CDTCJ-M', 4, 'Curso focado em testes unitários com JUnit e Mockito', 'ACTIVE', NULL, NOW());
