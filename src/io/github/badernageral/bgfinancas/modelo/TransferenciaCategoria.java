/*
Copyright 2012-2018 Jose Robson Mariano Alves

This file is part of bgfinancas.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This package is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.

*/
package io.github.badernageral.bgfinancas.modelo;

import io.github.badernageral.bgfinancas.biblioteca.contrato.Categoria;
import io.github.badernageral.bgfinancas.biblioteca.utilitario.Erro;
import io.github.badernageral.bgfinancas.biblioteca.contrato.Modelo;
import io.github.badernageral.bgfinancas.biblioteca.banco.Coluna;
import io.github.badernageral.bgfinancas.biblioteca.sistema.Janela;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class TransferenciaCategoria extends Categoria<TransferenciaCategoria> implements Modelo {
    
    private static final String MODULO = RAIZ+"/modulo/transferencia/categoria";
    
    public static final String FXML = MODULO+"/TransferenciaCategoria.fxml";
    public static final String FXML_FORMULARIO = MODULO+"/TransferenciaCategoriaFormulario.fxml";
    
    public static final String TABELA = "transferencias_categorias";
    
    public TransferenciaCategoria(){ 
        this(null, null);
    }

    public TransferenciaCategoria(String idCategoria, String nome){
        this.idCategoria = new Coluna(TABELA, "id_categoria", idCategoria);
        this.nome = new Coluna(TABELA, "nome", nome);
    }
    
    @Override
    protected TransferenciaCategoria instanciar(String idCategoria, String nome) {
        return new TransferenciaCategoria(idCategoria, nome);
    }
    
    @Override
    protected TransferenciaCategoria instanciar(ResultSet rs) throws SQLException {
        return new TransferenciaCategoria(
            rs.getString(idCategoria.getColuna()),
            rs.getString(nome.getColuna())
        );
    }
    
    @Override
    public boolean cadastrar(){
        return this.insert(nome).commit();
    }
    
    @Override
    public boolean alterar(){
        return this.update(nome).where(idCategoria, "=").commit();
    }
    
    @Override
    public boolean excluir() throws Erro{
        ObservableList<TransferenciaItem> itens = new TransferenciaItem().setIdCategoria(idCategoria.getValor()).listar();
        if(itens.size()>0){
            throw new Erro();
        }else{
            return this.delete(idCategoria, "=").commit();
        }
    }
    
    @Override
    public TransferenciaCategoria consultar(){
        try{
            this.select(idCategoria, nome);
            if(idCategoria.getValor() != null){
                this.where(idCategoria, "=");
            }else if(nome.getValor() != null){
                this.where(nome, "=");
            }
            ResultSet rs = this.query();
            if(rs != null && rs.next()){
                return instanciar(rs);
            }else{
                return null;
            }
        }catch(SQLException ex){
            Janela.showException(ex);
            return null;
        }
    }
    
    @Override
    public ObservableList<TransferenciaCategoria> listar(){
        try{
            this.select(idCategoria, nome);
            if(nome.getValor() != null){
                this.where(nome, "LIKE");
            }
            this.orderByAsc(nome);
            ResultSet rs = this.query();            
            if(rs != null){
                List<TransferenciaCategoria> Linhas = new ArrayList<>();
                while(rs.next()){
                    Linhas.add(instanciar(rs));
                }
                ObservableList<TransferenciaCategoria> Resultado = FXCollections.observableList(Linhas);
                return Resultado;
            }else{
                return null;
            }
        }catch(SQLException ex){
            Janela.showException(ex);
            return null;
        }
    }
    
    @Override
    protected TransferenciaCategoria getThis() { 
        return this; 
    }

}