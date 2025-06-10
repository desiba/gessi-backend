       

BEGIN
	IF NOT EXISTS (SELECT * FROM users where email = 'des@geesi.shop')
	BEGIN
		INSERT INTO users (id, firstName, lastName, email, password)
		VALUES ('1f5e3c25-8bdd-4fff-b167-a8165be32133', 'desmond', 'ayodeji', 'des@geesi.shop', 'P@ss123'),
      
	END 
END 