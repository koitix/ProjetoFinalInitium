package br.com.db1.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario", schema = "public")
public class Usuario  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false)
	private String nome;

	@Column(length = 50, nullable = false)
	private String email;

	@Column(length = 50)
	private byte[] senha;
	
	@Column()
	private Boolean administrador = false;

	@Column(nullable = false)
	private Boolean statusUsuario = false;
	
	@Column
	private byte[] foto;
	
	@Column(name = "nomeArquivo", nullable = false, length = 50)
	private String nomeArquivo;

	@Column(name = "extensao", nullable = false, length = 10)
	private String extensaoArquivo;
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getSenha() {
		return senha;
	}

	public void setSenha(byte[] senha) {
		this.senha = senha;
	}

	public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	public Boolean getStatusUsuario() {
		return statusUsuario;
	}

	public void setStatusUsuario(Boolean statusUsuario) {
		this.statusUsuario = statusUsuario;
	}
	
	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] arquivoByte) {
		this.foto = arquivoByte;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getExtensaoArquivo() {
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}
}
