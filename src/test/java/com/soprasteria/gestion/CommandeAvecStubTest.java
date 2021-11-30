package com.soprasteria.gestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.soprasteria.paiement.IPaiement;
import com.soprasteria.paiement.impl.PaiementAMEX;
import com.soprasteria.paiement.impl.PaiementMasterCard;
import com.soprasteria.paiement.impl.PaiementVISA;
import com.soprasteria.panier.model.Article;
import com.soprasteria.panier.model.Panier;
import com.soprasteria.panier.model.exceptions.MontantTropEleveException;
import com.soprasteria.panier.model.exceptions.QuantiteArticleTropGrandeException;
import com.soprasteria.panier.model.exceptions.TropDeReferencesException;

public class CommandeAvecStubTest {
	private static Client client;
	private static Panier pan;
	private static Article art1;


	@BeforeAll
	public static void init()
			throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		// Panier
		pan = new Panier();
		art1 = new Article(100.00, "REF001", "LIBELLE01", 9.99);
		pan.ajouterArticle(art1, 5);
		client = new Client();
	}

	
	
	class payment_stub implements IPaiement{

		@Override
		public boolean transaction(String refCommercant, String pan, String moisExpiration, String anneeExpiration,
				String cvv2, double montant) {
			// TODO Auto-generated method stub
			return true;
		}
		
		
	}
	
	@Test
	public void testValiderPaiement() throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		// TODO : créer un stub de IPaiement avec une classe anonyme permettant

		
		payment_stub p_s = new payment_stub();

	
		// preparation
		Commande commande = new Commande(client,pan,p_s);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification
		assertThat(resultat).isTrue();
	}
}
