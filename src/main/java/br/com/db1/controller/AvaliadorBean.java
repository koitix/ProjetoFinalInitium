package br.com.db1.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

import br.com.db1.dao.Transactional;
import br.com.db1.dao.impl.TipoAvaliacaoDao;
import br.com.db1.dao.impl.UsuarioDao;
import br.com.db1.model.TipoAvaliacao;
import br.com.db1.model.Usuario;
import br.com.db1.service.Criptografia;

@ApplicationScoped
@Named
public class AvaliadorBean  {	

	@Inject
	private UsuarioDao dao;
	
	@Inject
	private TipoAvaliacaoDao tipoAvaliacaoDao;
	
	@Inject
	private Criptografia criptografia;

	private List<Usuario> list;
	
	private List<TipoAvaliacao> listTipoAvaliacao;

	private String nomeUsuarioFiltrada;
	
	private Usuario usuario;
	
	private String senha;
	
	private String confirmarSenha;
	
	private Part arquivoUpado;
	
	public String getConfirmarSenha() {
		return confirmarSenha;
	}
	
	
	public Part getArquivoUpado() {
		return arquivoUpado;
	}

	public void setArquivoUpado(Part arquivoUpado) {
		this.arquivoUpado = arquivoUpado;
	}
	
	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	private void carregarTipoAvaliacao() {
		listTipoAvaliacao = tipoAvaliacaoDao.findAll();
	}


	public TipoAvaliacaoDao getTipoAvaliacaoDao() {
		return tipoAvaliacaoDao;
	}

	public void setTipoAvaliacaoDao(TipoAvaliacaoDao tipoAvaliacaoDao) {
		this.tipoAvaliacaoDao = tipoAvaliacaoDao;
	}

	public List<TipoAvaliacao> getListTipoAvaliacao() {
		return listTipoAvaliacao;
	}

	public void setListTipoAvaliacao(List<TipoAvaliacao> listTipoAvaliacao) {
		this.listTipoAvaliacao = listTipoAvaliacao;
	}

	@PostConstruct
	public void init() {
		zerarLista();
		carregarTipoAvaliacao();
		usuario = new Usuario();
	}

	private void zerarLista() {
		list = new ArrayList<Usuario>();
	}

	public String getNomeUsuarioFiltrada() {
		return nomeUsuarioFiltrada;
	}

	public void setNomeUsuarioFiltrada(String nomeUsuarioFiltrada) {
		this.nomeUsuarioFiltrada = nomeUsuarioFiltrada;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<Usuario> getList() {
		return list;
	}

	public String novo() {
		this.usuario = new Usuario();
		return "cadastrarAvaliador";
	}
	
	public String getNomeArquivo() {
		String header = arquivoUpado.getHeader("content-disposition");
		if (header == null)
			return "";
		for (String headerPart : header.split(";")) {
			if (headerPart.trim().startsWith("filename")) {
				return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return "";
	}
	
	public void importa() {
		try {
			this.usuario.setNomeArquivo(getNomeArquivo());
			this.usuario.setExtensaoArquivo(arquivoUpado.getContentType());

			byte[] arquivoByte = IOUtils.toByteArray(arquivoUpado.getInputStream());
			this.usuario.setFoto(arquivoByte);

		} catch (IOException e) {
			adicionarMensagem("Erro ao enviar o arquivo " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}
	}
	@Transactional
	public String salvar() {

		importa();
		if (senha.equals(confirmarSenha)) {
			if (this.usuario.getId() == null) {
				this.usuario.setSenha(criptografia.criptografar(senha, "MD5"));
			}
			if (!dao.save(this.usuario)) {
				adicionarMensagem("Erro ao cadastrar a Usuario.", FacesMessage.SEVERITY_ERROR);
			} else {
				adicionarMensagem("Usuario salva com sucesso.", FacesMessage.SEVERITY_INFO);
				nomeUsuarioFiltrada = this.usuario.getNome();
				listarUsuario();
			}
		} else {
			adicionarMensagem("As senhas n�o s�o iguais.", FacesMessage.SEVERITY_INFO);
		}
		return "avaliador";
	}

	
	public String editar(Usuario usuario) {
		this.usuario = dao.findById(usuario.getId());
		return "cadastrarAvaliador";
	}

	public String remover(Usuario usuario) {
		if (!dao.delete(usuario.getId())) {
			adicionarMensagem("Erro ao remover a Usuario.", FacesMessage.SEVERITY_ERROR);
		} else {
			adicionarMensagem("Usuario removida com sucesso.", FacesMessage.SEVERITY_INFO);
			listarUsuario();
		}
		return "avaliador";
	}

	public void listarUsuario() {
		zerarLista();
		if (!nomeUsuarioFiltrada.isEmpty()) {
			list.addAll(dao.findByNameAvaliador(nomeUsuarioFiltrada));
		} else {
			list.addAll(dao.findAvaliador());
		}
	}

	public void adicionarMensagem(String mensagem, Severity tipoMensagem) {
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage(mensagem);
		fm.setSeverity(tipoMensagem);
		fc.addMessage(null, fm);

	}
	
}