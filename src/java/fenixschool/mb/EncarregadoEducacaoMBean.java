/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fenixschool.mb;

import fenixschool.dao.EncarregadoEducacaoDAO;
import fenixschool.dao.ProfissaoDAO;
import fenixschool.modelo.EncarregadoEducacao;
import fenixschool.modelo.Profissao;
import fenixschool.modelo.Sexo;
import fenixschool.util.FicheiroUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author PENA
 */


@ManagedBean(name = "encarregadoEducacaoMBean")
@SessionScoped

public class EncarregadoEducacaoMBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private EncarregadoEducacao encarregadoEducacao;

    private List<EncarregadoEducacao> encarregadoEducacoes;
    private List<Profissao> profissoes;
    private ProfissaoDAO profissaoDAO;
    private EncarregadoEducacaoDAO encarregadoEducacaoDAO;

    @PostConstruct
    public void inicializar() {

        encarregadoEducacao = new EncarregadoEducacao();
        encarregadoEducacaoDAO = new EncarregadoEducacaoDAO();
        encarregadoEducacoes = new ArrayList<>();

        profissaoDAO = new ProfissaoDAO();
        profissoes = new ArrayList<>();
    }

    public EncarregadoEducacao getEncarregadoEducacao() {
        return encarregadoEducacao;
    }

    public void setEncarregadoEducacao(EncarregadoEducacao encarregadoEducacao) {
        this.encarregadoEducacao = encarregadoEducacao;
    }

    public List<EncarregadoEducacao> getEncarregadoEducacoes() {
        encarregadoEducacoes = encarregadoEducacaoDAO.findAll();
        return encarregadoEducacoes;
    }

    public void setEncarregadoEducacoes(List<EncarregadoEducacao> encarregadoEducacoes) {
        this.encarregadoEducacoes = encarregadoEducacoes;
    }

    public void guardar(ActionEvent evt) {

        encarregadoEducacaoDAO.save(encarregadoEducacao);
        encarregadoEducacao = new EncarregadoEducacao();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Guardar", "Encarregado registado com sucesso!"));
    }

    public List<Profissao> getProssifoes() {
        profissoes = profissaoDAO.findAll();
        return profissoes;
    }

    public List<SelectItem> getOpSexos() {
        List<SelectItem> list = new ArrayList<>();
        for (Sexo sexo : Sexo.values()) {
            list.add(new SelectItem(sexo, sexo.getAbreviatura()));
        }
        return list;
    }

    public void setProssifoes(List<Profissao> profissoes) {
        this.profissoes = profissoes;
    }

    public void fileUpload(FileUploadEvent event) {

        try {

            //cria um objecto do tipo UploadedFile para receber o objecto
            UploadedFile arquivo = event.getFile();

            //Transforma o objecto em Byte para ser guardado no banco de dados
            byte[] foto = IOUtils.toByteArray(arquivo.getInputstream());
            encarregadoEducacao.setFotoEncarregado(foto);
            encarregadoEducacao.setUrlFotoEncarregado(arquivo.getFileName());

            //comandos para guardar o objecto numa pasta local ou num disco duro
            InputStream in = new BufferedInputStream(arquivo.getInputstream());
               
           File file = new File(FicheiroUtil.getPathPastaAplicacaoJSF() + arquivo.getFileName());
           

            //Comandos para guardar no disco em rede
            // File file = new File("\\\\192.168.0.18\\photo\\fratiofmcpa"+arquivo.getFileName());
            FileOutputStream fout = new FileOutputStream(file);
            while (in.available() != 0) {
                fout.write(in.read());
            }
            fout.close();

            FacesMessage msg = new FacesMessage("Foto: ", arquivo.getFileName() + " carregada com sucesso!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

    }

    public void edit(java.awt.Event event) {
        encarregadoEducacaoDAO.update(encarregadoEducacao);
        encarregadoEducacao = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Actualizar", "Encarregado actualizado com sucesso!"));
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("encarregado_educacao_listar.jsf");

        } catch (IOException e) {
            java.util.logging.Logger.getLogger(EncarregadoEducacaoMBean.class
                    .getName()).log(Level.SEVERE, null, e);
        }
    }

    public String delete() {
        encarregadoEducacaoDAO.delete(encarregadoEducacao);
        encarregadoEducacao = null;
        return "encarregado_educacao_listar?faces-redirect=true";
    }

}
