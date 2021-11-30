package com.soprasteria.gestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soprasteria.gestion.CommandeAvecStubTest.payment_stub;
import com.soprasteria.paiement.exception.PaiementException;
import com.soprasteria.panier.model.Article;
import com.soprasteria.panier.model.Panier;
import com.soprasteria.panier.model.exceptions.MontantTropEleveException;
import com.soprasteria.panier.model.exceptions.QuantiteArticleTropGrandeException;
import com.soprasteria.panier.model.exceptions.TropDeReferencesException;

@ExtendWith(MockitoExtension.class)
public class CommandeAvecMockTest {

	private static Client client;
	private static Panier pan;
	private static Article art1;
	
	@Mock
	payment_stub p_s;

	@BeforeAll
	public static void init()
			throws TropDeReferencesException, QuantiteArticleTropGrandeException, MontantTropEleveException {
		// Panier
		pan = new Panier();
		art1 = new Article(100.00, "REF001", "LIBELLE01", 9.99);
		pan.ajouterArticle(art1, 5);
		client = new Client();
	}

	@Test
	public void testValiderPaiementMockInstance() {
		// TODO : instancier un mock de IPaiement et le configurer
		// afin que le test passe l'assertion
		payment_stub p_s2 = mock(payment_stub.class);
		when(p_s2.transaction(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble())).thenReturn(true);


		// preparation
		Commande commande = new Commande(client, pan, p_s2);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification mock
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande
		verify(p_s2, times(1)).transaction("ENSIM_COMMERCE", "4444555551666666", "01", "2017", "345", pan.prixTTCPanier());

		// vérification système testé
		assertThat(resultat).isTrue();
	}

	@Test
	public void testValiderPaiementMockAnnotation() {
		// TODO : configurer le mock par annotation (à définir) de IPaiement
		// afin que le test passe l'assertion
		when(p_s.transaction(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble())).thenReturn(true);

		// preparation
		
		Commande commande = new Commande(client, pan, p_s);

		// execution
		boolean resultat = commande.validerPaiement("4444555551666666", "01/2017", "345");

		// verification mock
		// TODO : vérifier l'invocation de la méthode transaction sur le mock
		// avec les paramètres pan=4444555551666666, moisExpiration=01,
		// anneeExpiration=2017, cvv2=345 et le montant attendu selon le panier
		// passé à la commande
		verify(p_s, times(1)).transaction("ENSIM_COMMERCE", "4444555551666666", "01", "2017", "345", pan.prixTTCPanier());

		// vérification système testé
		assertThat(resultat).isTrue();
	}

	@Test
	public void testValiderPaiementMockException() {
		// TODO : configurer le mock par annotation (à définir) de IPaiement
		// afin que le test passe l'assertion quel que soit les paramètres
		// d'appel de la dépendance
		when(p_s.transaction(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble())).thenReturn(true);
		when(p_s.transaction(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyDouble())).thenThrow(PaiementException.class);

		// preparation
		Commande commande = new Commande(client, pan, p_s);
		
		// verification
		assertThrows(PaiementException.class, () -> {			
			// execution
			commande.validerPaiement("4444555551666666", "01/2017", "345");
		});

	}

}
