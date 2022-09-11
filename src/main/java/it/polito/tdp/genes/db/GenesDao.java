package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String, Genes> idMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getString("GeneID"))) {
					Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
					idMap.put(genes.getGeneId(), genes);
				}
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Genes> getAllEssentialGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes WHERE Essential = 'Essential'";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAllAdiacenze(Map<String, Genes> idMap){
		String sql = "SELECT i.GeneID1 AS g1, i.GeneID2 AS g2, corrispondenza1.Chromosome as c1, corrispondenza2.Chromosome as c2, Expression_Corr as corr "
				+ "FROM (SELECT DISTINCT GeneID, Chromosome "
				+ "FROM genes) AS corrispondenza1, "
				+ "(SELECT DISTINCT GeneID, Chromosome "
				+ "FROM genes) AS corrispondenza2, "
				+ "interactions i "
				+ "WHERE i.GeneID1 = corrispondenza1.GeneId AND i.GeneID2 = corrispondenza2.GeneId AND corrispondenza1.Chromosome != 0 AND corrispondenza2.Chromosome != 0 "
				+ "AND i.GeneID1 != i.GeneID2 AND i.GeneID1 IN (SELECT GeneID FROM Genes WHERE Essential = 'Essential') "
				+ "AND i.GeneID2 IN (SELECT GeneID FROM Genes WHERE Essential = 'Essential')";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				int cromosoma1 = res.getInt("c1");
				int cromosoma2 = res.getInt("c2");
				
				Genes g1 = idMap.get(res.getString("g1"));
				Genes g2 = idMap.get(res.getString("g2"));
				
				if (cromosoma1 == cromosoma2) {
					Adiacenza a = new Adiacenza(g1, g2, 2*Math.abs(res.getDouble("corr")));
					result.add(a);
				} else {
					Adiacenza a = new Adiacenza(g1, g2, Math.abs(res.getDouble("corr")));
					result.add(a);
				}
				
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
