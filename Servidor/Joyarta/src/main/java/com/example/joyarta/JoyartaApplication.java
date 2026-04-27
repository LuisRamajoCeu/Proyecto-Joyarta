package com.example.joyarta;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.joyarta.model.Categoria;
import com.example.joyarta.model.Perfil;
import com.example.joyarta.model.Producto;
import com.example.joyarta.model.Usuario;
import com.example.joyarta.repository.CategoriaRepository;
import com.example.joyarta.repository.ProductoRepository;
import com.example.joyarta.repository.UsuarioRepository;
@SpringBootApplication
public class JoyartaApplication implements CommandLineRunner{

	public static void main(String[] args) {
		try {
			String url = "jdbc:mariadb://localhost:3306/";
			try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url + "jpa", "luis", "luis")) {
			} catch (Exception e) {
				System.out.println("No se pudo conectar como luis. Intentando crear usuario con root...");
				try (java.sql.Connection rootConn = java.sql.DriverManager.getConnection(url, "root", "luis");
					 java.sql.Statement stmt = rootConn.createStatement()) {
					stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS jpa");
					stmt.executeUpdate("CREATE USER IF NOT EXISTS 'luis'@'localhost' IDENTIFIED BY 'luis'");
					stmt.executeUpdate("CREATE USER IF NOT EXISTS 'luis'@'%' IDENTIFIED BY 'luis'");
					stmt.executeUpdate("GRANT ALL PRIVILEGES ON jpa.* TO 'luis'@'localhost'");
					stmt.executeUpdate("GRANT ALL PRIVILEGES ON jpa.* TO 'luis'@'%'");
					stmt.executeUpdate("FLUSH PRIVILEGES");
					System.out.println("Usuario luis y base de datos jpa creados exitosamente.");
				}
			}
		} catch (Exception ex) {
			System.err.println("Fallo la inicializacion de la base de datos: " + ex.getMessage());
		}
		SpringApplication.run(JoyartaApplication.class, args);
	}

	@Autowired ProductoRepository productoRepo;
	@Autowired CategoriaRepository categoriaRepo;
	@Autowired UsuarioRepository usuarioRepo;
	
	@Override
	public void run(String... args) {
    Usuario maria = usuarioRepo.findByEmail("maria@joyarta.com");
    if (maria == null) {
        Perfil perfilMaria = new Perfil();
        perfilMaria.setNombre("Maria Silversmith");
        perfilMaria.setBiografia("Especialista en anillos y plata de ley 925");
        
        maria = new Usuario();
        maria.setNombre("Maria Silversmith");
        maria.setEmail("maria@joyarta.com");
        maria.setPassword("1234");
        maria.setRol("ARTESANO");
        maria.setFechaRegistro(LocalDate.now());
        maria.setPerfil(perfilMaria);
        usuarioRepo.save(maria);
    }

    Usuario carlos = usuarioRepo.findByEmail("carlos@joyarta.com");
    if (carlos == null) {
        Perfil perfilCarlos = new Perfil();
        perfilCarlos.setNombre("Carlos Goldwork");
        perfilCarlos.setBiografia("Especialista en oro y joyas premium");
        
        carlos = new Usuario();
        carlos.setNombre("Carlos Goldwork");
        carlos.setEmail("carlos@joyarta.com");
        carlos.setPassword("1234");
        carlos.setRol("ARTESANO");
        carlos.setFechaRegistro(LocalDate.now());
        carlos.setPerfil(perfilCarlos);
        usuarioRepo.save(carlos);
    }

    Usuario ana = usuarioRepo.findByEmail("ana@joyarta.com");
    if (ana == null) {
        Perfil perfilAna = new Perfil();
        perfilAna.setNombre("Ana Beadmaker");
        perfilAna.setBiografia("Especialista en collares y colgantes artesanales");
        
        ana = new Usuario();
        ana.setNombre("Ana Beadmaker");
        ana.setEmail("ana@joyarta.com");
        ana.setPassword("1234");
        ana.setRol("ARTESANO");
        ana.setFechaRegistro(LocalDate.now());
        ana.setPerfil(perfilAna);
        usuarioRepo.save(ana);
    }

    Usuario luis = usuarioRepo.findByEmail("luis@joyarta.com");
    if (luis == null) {
        Perfil perfilLuis = new Perfil();
        perfilLuis.setNombre("Luis Craftsman");
        perfilLuis.setBiografia("Especialista en pendientes y pulseras");
        
        luis = new Usuario();
        luis.setNombre("Luis Craftsman");
        luis.setEmail("luis@joyarta.com");
        luis.setPassword("1234");
        luis.setRol("ARTESANO");
        luis.setFechaRegistro(LocalDate.now());
        luis.setPerfil(perfilLuis);
        usuarioRepo.save(luis);
    }

    if (categoriaRepo.count() == 0) {
        Categoria cAnillos = new Categoria("Anillos", "Anillos hechos a mano");
        Categoria cCollares = new Categoria("Collares", "Collares y colgantes únicos");
        Categoria cPendientes = new Categoria("Pendientes", "Pendientes de plata y oro");
        Categoria cPulseras = new Categoria("Pulseras", "Brazaletes y pulseras artesanales");
        
        categoriaRepo.saveAll(Arrays.asList(cAnillos, cCollares, cPendientes, cPulseras));
    }

    if (productoRepo.count() == 0) {
        maria = usuarioRepo.findByEmail("maria@joyarta.com");
        carlos = usuarioRepo.findByEmail("carlos@joyarta.com");
        ana = usuarioRepo.findByEmail("ana@joyarta.com");
        luis = usuarioRepo.findByEmail("luis@joyarta.com");
        
        List<Categoria> categorias = categoriaRepo.findAll();
        Categoria cAnillos = categorias.stream().filter(c -> c.getNombre().equals("Anillos")).findFirst().orElse(null);
        Categoria cCollares = categorias.stream().filter(c -> c.getNombre().equals("Collares")).findFirst().orElse(null);
        Categoria cPendientes = categorias.stream().filter(c -> c.getNombre().equals("Pendientes")).findFirst().orElse(null);
        Categoria cPulseras = categorias.stream().filter(c -> c.getNombre().equals("Pulseras")).findFirst().orElse(null);

        List<Producto> catalogo = new ArrayList<>();

        catalogo.add(crearProducto("Anillo Atlante de Plata", "Anillo protector con geometría sagrada, hecho en plata de ley 925.", 45.50, 
            "https://i.pinimg.com/736x/c4/91/5f/c4915f17789adba68e7724ed310a435b.jpg", maria, cAnillos));
        
        catalogo.add(crearProducto("Anillo Minimalista Cobre", "Diseño fino y moderno en cobre pulido.", 15.00, 
            "https://imgs.search.brave.com/aJaWCzkl0vqWy9hcxB9i3PEMNpoGuCl0ihEBni9NLbA/rs:fit:860:0:0:0/g:ce/aHR0cHM6Ly9pLmV0/c3lzdGF0aWMuY29t/LzYwODkwNTIvci9p/bC9hYTYxNjkvNTQ4/MzEyMjMxNy9pbF83/OTR4Ti41NDgzMTIy/MzE3X2ttaGcuanBn", maria, cAnillos));
            
        catalogo.add(crearProducto("Sortija con Rubí", "Piedra preciosa roja sobre base de plata vieja.", 89.90, 
            "https://images.unsplash.com/photo-1573408301185-9146fe634ad0?auto=format&fit=crop&w=500&q=60", maria, cAnillos));
            
        catalogo.add(crearProducto("Set de Anillos Boho", "Juego de 5 anillos para combinar.", 25.00, 
            "https://ladypilita.myshopify.com/cdn/shop/products/HTB1Jrh2OFXXXXcVXFXXq6xXFXXXs_large.jpg?v=1574240528", maria, cAnillos));

        catalogo.add(crearProducto("Brazalete Rígido", "Plata maciza con grabado personalizado.", 75.00, 
            "https://i.etsystatic.com/5725644/r/il/385fe6/1368932927/il_570xN.1368932927_570g.jpg", maria, cPulseras));

        catalogo.add(crearProducto("Sello Real de Oro", "Sello clásico bañado en oro de 18k. Ideal para regalo.", 120.00, 
            "https://dam.elcorteingles.es/producto/www-4051245429770-00.jpg?impolicy=Resize&width=1200&height=1200", carlos, cAnillos));

        catalogo.add(crearProducto("Collar de Perlas", "Perlas de río auténticas con broche de plata.", 65.00, 
            "https://nativos-jewelry.com/cdn/shop/files/perlas_de_rio_nnue_1280x.jpg?v=1729900416", carlos, cCollares));
            
        catalogo.add(crearProducto("Gargantilla Dorada", "Estilo choker pegado al cuello.", 35.50, 
            "https://images.unsplash.com/photo-1602173574767-37ac01994b2a?auto=format&fit=crop&w=500&q=60", carlos, cCollares));
            
        catalogo.add(crearProducto("Colgante Corazón Cristal", "Cristal de Swarovski color azul océano.", 42.00, 
            "https://images.unsplash.com/photo-1535632787350-4e68ef0ac584?auto=format&fit=crop&w=500&q=60", carlos, cCollares));

        catalogo.add(crearProducto("Aros de Oro Grandes", "Aros clásicos de 5cm de diámetro.", 55.00, 
            "https://i.etsystatic.com/26837950/r/il/2e5273/6278307967/il_fullxfull.6278307967_c89t.jpg", carlos, cPendientes));

        catalogo.add(crearProducto("Colgante Árbol de la Vida", "Colgante tallado en madera de olivo con incrustaciones de turquesa.", 29.99, 
            "https://i.etsystatic.com/6874770/r/il/34344b/6892542774/il_300x300.6892542774_cf99.jpg", ana, cCollares));

        catalogo.add(crearProducto("Colgante Árbol Vida", "Madera de olivo y turquesa.", 29.99, 
            "https://m.media-amazon.com/images/I/61nyO4e40QL._AC_UY1000_.jpg", ana, cCollares));

        catalogo.add(crearProducto("Pendientes Gota de Agua", "Pendientes minimalistas de cristal azul y gancho de plata.", 15.00, 
            "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=500&q=60", ana, cPendientes));

        catalogo.add(crearProducto("Pendientes Vintage", "Estilo años 20 con piedras oscuras.", 28.00, 
            "https://i.etsystatic.com/36844122/r/il/902008/4056207998/il_570xN.4056207998_5onl.jpg", ana, cPendientes));

        catalogo.add(crearProducto("Pulsera de Cuero", "Cuero trenzado marrón con cierre magnético.", 18.90, 
            "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?auto=format&fit=crop&w=500&q=60", ana, cPulseras));

        catalogo.add(crearProducto("Pendientes Gota Agua", "Cristal azul y gancho de plata.", 15.00, 
            "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=500&q=60", luis, cPendientes));

        catalogo.add(crearProducto("Pendientes Pluma", "Plata labrada con forma de pluma.", 22.50, 
            "https://raivejoyas.com/cdn/shop/products/AG-11.jpg?v=1679920476", luis, cPendientes));

        catalogo.add(crearProducto("Pulsera Piedras Chakra", "7 piedras naturales para la energía.", 20.00, 
            "https://www.mahana-monoi.com/1189-home_default/pulsera-de-los-7-chakras.jpg", luis, cPulseras));

        catalogo.add(crearProducto("Anillo Atlante Plata 925", "Protector con geometría sagrada, plata 925.", 45.50, 
            "https://images.unsplash.com/photo-1605100804763-247f67b3557e?auto=format&fit=crop&w=500&q=60", luis, cAnillos));

        catalogo.add(crearProducto("Sello Oro 18k", "Sello clásico bañado en oro de 18k.", 120.00, 
            "https://i.etsystatic.com/18841598/r/il/b69746/4702101655/il_570xN.4702101655_qyrn.jpg", luis, cAnillos));

        catalogo.add(crearProducto("Brazalete Celta de Plata", "Brazalete con nudos celtas en plata de ley 925.", 68.00, 
            "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?auto=format&fit=crop&w=500&q=60", ana, cPulseras));

        catalogo.add(crearProducto("Collar Turquesa Natural", "Turquesas auténticas con cadena de plata.", 52.00, 
            "https://images.unsplash.com/photo-1602173574767-37ac01994b2a?auto=format&fit=crop&w=500&q=60", carlos, cCollares));

        catalogo.add(crearProducto("Anillo Trenza Dorada", "Diseño trenzado bañado en oro rosa.", 38.50, 
            "https://images.unsplash.com/photo-1605100804763-247f67b3557e?auto=format&fit=crop&w=500&q=60", ana, cAnillos));

        catalogo.add(crearProducto("Pendientes Luna y Estrella", "Par asimétrico en plata con zirconia.", 32.00, 
            "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=500&q=60", maria, cPendientes));

        catalogo.add(crearProducto("Pulsera Infinito", "Símbolo infinito en plata con cadena ajustable.", 24.00, 
            "https://images.unsplash.com/photo-1611591437281-460bfbe1220a?auto=format&fit=crop&w=500&q=60", carlos, cPulseras));

        productoRepo.saveAll(catalogo);
        System.out.println("✅ CATÁLOGO COMPLETO CARGADO: " + catalogo.size() + " PRODUCTOS con 4 ARTESANOS");
    }

	}
	private Producto crearProducto(String nombre, String desc, double precio, String img, Usuario u, Categoria c) {
		Producto p = new Producto();
		p.setNombre(nombre);
		p.setDescripcion(desc);
		p.setPrecio(precio);
		p.setImagenUrl(img);
		p.setUsuario(u);
		p.setCategoria(c);
		p.setStock(10); 
		return p;
	}
    

	
}
