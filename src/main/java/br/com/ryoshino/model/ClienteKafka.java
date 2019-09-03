package br.com.ryoshino.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class ClienteKafka {
    @Id
    private Long idCliente;

    private String nome;
    private String endereco;
    private Integer telefone;
    private String email;
    private Integer cpf;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAtualizacao;

    public ClienteKafka(Long idCliente, String nome, String endereco, Integer telefone, String email, Integer cpf, LocalDate dataAtualizacao) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
        this.dataAtualizacao = dataAtualizacao;
    }

    public ClienteKafka() {

    }

    public Long getIdCliente() {
        return idCliente;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public Integer getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCpf() {
        return cpf;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public String toString() {
        return "ClienteKafka{" +
                "idCliente=" + idCliente +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", telefone=" + telefone +
                ", email='" + email + '\'' +
                ", cpf=" + cpf +
                '}';
    }
}
