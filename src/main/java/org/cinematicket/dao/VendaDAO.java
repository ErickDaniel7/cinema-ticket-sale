package org.cinematicket.dao;

import org.cinematicket.model.Venda;
import org.cinematicket.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class VendaDAO {

    // Salvar ou atualizar uma venda
    public void salvar(Venda venda) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(venda);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Listar todas as vendas
    public List<Venda> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Venda> query = session.createQuery("from Venda", Venda.class);
            return query.list();
        }
    }

    // Buscar venda pelo ID
    public Venda buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Venda.class, id);
        }
    }

    // Cancelar venda (seta cancelado = true)
    public void cancelarVenda(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Venda venda = session.get(Venda.class, id);
            if (venda != null) {
                venda.setCancelado(true);
                session.update(venda);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // Editar venda (salva as alterações da venda já existente)
    public void editar(Venda venda) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(venda);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
