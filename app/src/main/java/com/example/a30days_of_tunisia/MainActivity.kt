package com.example.a30days_of_tunisia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a30days_of_tunisia.ui.theme.*

// =====================================================================
// MODEL
// =====================================================================

enum class PlaceType(val label: String) {
    COAST("Côte et nature"),
    ANTIQUITY("Antiquité"),
    DESERT("Désert et oasis"),
    MEDINA("Médina")
}

data class Place(
    val day: Int,
    val name: String,
    val summary: String,
    val details: String,
    val tip: String,
    val type: PlaceType,
    @get:DrawableRes val image: Int
)

// =====================================================================
// DATA : the 30 places
// =====================================================================

val places = listOf(
    Place(1, "Sidi Bou Saïd", "Le village bleu et blanc au-dessus de la mer",
        "Perché sur une falaise au nord de Tunis, Sidi Bou Saïd est célèbre pour ses maisons blanchies à la chaux, ses portes et ses fenêtres bleues et ses ruelles pavées. La vue sur le golfe de Tunis est superbe.",
        "Venez tôt le matin pour éviter la foule, puis descendez jusqu'au port à pied.", PlaceType.COAST, R.drawable.day_01),
    Place(2, "Médina de Tunis", "Un labyrinthe de souks classé à l'UNESCO",
        "La médina de Tunis est inscrite au patrimoine mondial depuis 1979. On y trouve la mosquée Zitouna, des palais, des medersas et des souks spécialisés : parfums, tissus, cuivre et chéchias.",
        "Entrez par Bab el Bhar et laissez-vous guider par les ruelles. Marchandez avec le sourire.", PlaceType.MEDINA, R.drawable.day_02),
    Place(3, "Carthage", "Les ruines de l'ancienne rivale de Rome",
        "Selon la tradition, Carthage a été fondée par les Phéniciens au IXe siècle avant J.-C. On y visite la colline de Byrsa et les thermes d'Antonin, l'un des plus grands ensembles de thermes romains d'Afrique, face à la mer.",
        "Combinez la visite avec Sidi Bou Saïd, les deux sites sont proches.", PlaceType.ANTIQUITY, R.drawable.day_03),
    Place(4, "Musée du Bardo", "Une collection de mosaïques romaines exceptionnelle",
        "Installé dans un ancien palais beylical, le musée du Bardo abrite l'une des plus riches collections de mosaïques romaines au monde, ainsi que des statues et des objets antiques.",
        "Prévoyez deux à trois heures et regardez aussi les plafonds du palais.", PlaceType.ANTIQUITY, R.drawable.day_04),
    Place(5, "La Marsa", "La corniche, la plage et les cafés",
        "La Marsa est une ville balnéaire au nord de Tunis. Les habitants viennent s'y promener sur la corniche, se baigner en été et s'attabler dans les cafés avec vue sur la mer.",
        "Allez-y en fin de journée pour le coucher du soleil.", PlaceType.COAST, R.drawable.day_05),
    Place(6, "Hammamet", "Le jasmin, la médina et le golfe",
        "Hammamet est une station balnéaire réputée pour ses plages de sable fin, sa petite médina et sa kasbah qui domine la mer. Le jasmin est le symbole de la ville.",
        "Montez sur les remparts de la kasbah pour la vue sur le golfe.", PlaceType.COAST, R.drawable.day_06),
    Place(7, "Kélibia", "Une forteresse au-dessus de la plage",
        "Kélibia est une ville du Cap Bon, au bord de la Méditerranée. Elle est dominée par une forteresse construite sur une colline, d'où l'on voit le port de pêche, la mer et, par temps clair, la côte voisine. La ville est connue pour ses plages de sable blanc aux eaux turquoise, pour son vin muscat et pour son festival international du film amateur.",
        "Montez à la forteresse en fin de journée pour la vue, puis descendez vers la plage pour la baignade.", PlaceType.COAST, R.drawable.day_07),
    Place(8, "Cap Bon", "Une presqu'île d'agrumes et de plages sauvages",
        "Le Cap Bon avance dans la Méditerranée en direction de la Sicile. La région est couverte d'orangers et de citronniers, et El Haouaria est connue pour ses grottes romaines et ses rapaces.",
        "Louez une voiture pour explorer les petites criques à votre rythme.", PlaceType.COAST, R.drawable.day_08),
    Place(9, "Zaghouan", "Le Temple des Eaux au pied de la montagne",
        "À Zaghouan, un temple romain du IIe siècle marque la source qui alimentait Carthage grâce à un long aqueduc. La montagne voisine offre de belles randonnées.",
        "Combinez le temple avec un tronçon de l'aqueduc, visible depuis la route.", PlaceType.ANTIQUITY, R.drawable.day_09),
    Place(10, "Dougga", "L'une des villes romaines les mieux conservées",
        "Classé à l'UNESCO, le site de Dougga domine la campagne. On y voit un théâtre, le Capitole, des temples et des rues antiques, dans un cadre de collines.",
        "Prenez de bonnes chaussures et de l'eau : le site est vaste et peu ombragé.", PlaceType.ANTIQUITY, R.drawable.day_10),
    Place(11, "Korbous", "Des sources chaudes au bord de la mer",
        "Korbous est une petite station thermale du Cap Bon, entre falaises et forêt, face à la Méditerranée. Ses sources d'eau chaude, connues depuis l'Antiquité, sortent de la roche et certaines coulent jusqu'à la mer. On y vient depuis longtemps pour se détendre et se soigner, dans un décor de côte sauvage.",
        "Prenez un bain dans un établissement thermal, puis terminez la journée sur la corniche pour la vue sur la mer.", PlaceType.COAST, R.drawable.day_11),
    Place(12, "Tabarka", "Un fort génois et une côte corallienne",
        "Tabarka est une petite ville du nord-ouest, connue pour son fort génois bâti sur un îlot, ses fonds marins et son festival de jazz. La plongée y est très populaire.",
        "Essayez la plongée ou le snorkeling pour découvrir les fonds marins.", PlaceType.COAST, R.drawable.day_12),
    Place(13, "Aïn Draham", "Les forêts de chênes-lièges de la Kroumirie",
        "Dans le nord-ouest, Aïn Draham est entourée de forêts de chênes-lièges et de chênes zéen. C'est l'une des régions les plus arrosées du pays, avec un air frais et vert.",
        "Idéal pour la randonnée au printemps et en automne.", PlaceType.COAST, R.drawable.day_13),
    Place(14, "Bizerte", "Un vieux port plein de charme",
        "Bizerte est une ville portuaire du nord, avec son vieux port, sa kasbah et son canal qui relie la mer au lac. Les cafés du quai y sont très animés.",
        "Promenez-vous au vieux port et goûtez le poisson frais.", PlaceType.COAST, R.drawable.day_14),
    Place(15, "Kairouan", "La ville sainte et sa Grande Mosquée",
        "Fondée en 670, Kairouan est l'un des grands centres religieux du monde musulman. Sa Grande Mosquée, avec sa cour immense et sa salle de prière à nombreuses colonnes, est un chef-d'œuvre. La ville est aussi connue pour ses tapis et ses pâtisseries.",
        "Goûtez le makroudh, une pâtisserie de semoule et de dattes.", PlaceType.MEDINA, R.drawable.day_15),
    Place(16, "El Jem", "Un amphithéâtre romain gigantesque",
        "L'amphithéâtre d'El Jem date du IIIe siècle et pouvait accueillir des dizaines de milliers de spectateurs. Il est classé à l'UNESCO et reste très bien conservé.",
        "Descendez dans les souterrains où l'on gardait fauves et gladiateurs.", PlaceType.ANTIQUITY, R.drawable.day_16),
    Place(17, "Sousse", "La médina et le ribat au bord de la mer",
        "La médina de Sousse est classée à l'UNESCO. Le ribat, forteresse-monastère du VIIIe siècle, et sa tour offrent une vue panoramique sur la ville et la mer.",
        "Montez en haut de la tour du ribat pour le panorama.", PlaceType.MEDINA, R.drawable.day_17),
    Place(18, "Monastir", "Un ribat face à la Méditerranée",
        "Monastir possède l'un des plus anciens ribats du pays, bâti à la fin du VIIIe siècle. À proximité se trouve le mausolée de Habib Bourguiba, premier président de la Tunisie.",
        "Visitez le ribat le matin, quand la lumière est douce.", PlaceType.MEDINA, R.drawable.day_18),
    Place(19, "Mahdia", "Un cap africain et un port de pêche",
        "Mahdia a été fondée en 916 et fut la capitale des Fatimides. On y découvre la Skifa el Kahla, ancienne porte monumentale, et un port de pêche coloré.",
        "Allez au port tôt le matin pour voir les pêcheurs rentrer.", PlaceType.COAST, R.drawable.day_19),
    Place(20, "Béja", "Le grenier à blé de la Tunisie",
        "Béja est une ville du nord-ouest, entourée de collines et de grandes plaines fertiles où l'on cultive surtout les céréales. Dans l'Antiquité, cette région fournissait beaucoup de blé à Rome. La ville est dominée par une forteresse byzantine, et sa vieille médina se découvre en montant dans les ruelles. Le gouvernorat abrite aussi le site romain de Dougga, et la ville accueille un festival d'été.",
        "Montez à la forteresse pour voir la ville et la campagne, puis visitez la médina à pied.", PlaceType.MEDINA, R.drawable.day_20),
    Place(21, "Djerba", "L'île de Houmt Souk et de la Ghriba",
        "Djerba est une île au sud-est du pays, avec ses plages, ses mosquées blanches et sa ville de Houmt Souk. La synagogue de la Ghriba est l'une des plus anciennes d'Afrique du Nord.",
        "Louez un vélo pour explorer les villages et les petites routes.", PlaceType.COAST, R.drawable.day_21),
    Place(22, "Guellala", "La poterie traditionnelle de Djerba",
        "Le village de Guellala fabrique depuis longtemps de la poterie en argile locale. Les ateliers montrent encore les gestes traditionnels, du tournage à la cuisson.",
        "Demandez à voir le four et rapportez une petite pièce en souvenir.", PlaceType.COAST, R.drawable.day_22),
    Place(23, "Matmata", "Des maisons creusées dans la terre",
        "À Matmata, les habitants ont creusé leurs maisons autour d'un grand puits central pour se protéger de la chaleur. Certaines habitations se visitent encore.",
        "Entrez dans une maison troglodyte pour sentir la fraîcheur du sol.", PlaceType.DESERT, R.drawable.day_23),
    Place(24, "Tataouine et ses ksour", "Des villages fortifiés berbères",
        "Dans la région de Tataouine, les ksour sont des greniers fortifiés composés de petites salles superposées appelées ghorfas. Ksar Ouled Soltane et Ksar Hadada en sont de beaux exemples.",
        "Montez sur les terrasses pour admirer la vue sur la plaine.", PlaceType.DESERT, R.drawable.day_24),
    Place(25, "Chenini", "Un village berbère perché sur la montagne",
        "Chenini est un village berbère accroché à une crête, avec des maisons en pierre, des habitations troglodytes et une mosquée blanche. La vue sur la plaine est impressionnante.",
        "Visitez-le au lever du jour, la lumière sur la pierre est magnifique.", PlaceType.DESERT, R.drawable.day_25),
    Place(26, "Douz", "La porte du Sahara",
        "Douz est la dernière grande ville avant les dunes du Grand Erg Oriental. Elle est célèbre pour ses dromadaires et son festival international du Sahara.",
        "Faites une balade à dos de dromadaire au coucher du soleil.", PlaceType.DESERT, R.drawable.day_26),
    Place(27, "Médenine", "Une ville carrefour du sud et ses ghorfas",
        "Médenine est une ville du sud-est tunisien, située à la croisée des routes vers Djerba, Tataouine et Matmata. Elle s'est développée autour de ses ksour, des greniers fortifiés formés de petites pièces voûtées superposées appelées ghorfas. Autour de la ville, les paysages deviennent semi-désertiques, ce qui en fait une bonne étape avant de continuer vers le sud.",
        "Visitez les ghorfas du ksar de Médenine, puis continuez vers Tataouine pour voir d'autres ksour.", PlaceType.DESERT, R.drawable.day_27),
    Place(28, "Chott el-Djérid", "Un immense lac salé en plein désert",
        "Le Chott el-Djérid est le plus grand lac salé de Tunisie. Sa surface blanche et ses reflets créent parfois des mirages. Les couleurs changent à l'aube et au crépuscule.",
        "Arrêtez-vous au coucher du soleil pour de belles photos.", PlaceType.DESERT, R.drawable.day_28),
    Place(29, "Chebika et Tamerza", "Des oasis de montagne et leurs cascades",
        "Près de la frontière algérienne, Chebika et Tamerza sont des oasis nichées dans les montagnes, avec des canyons, des palmiers et de petites cascades.",
        "Portez de bonnes chaussures pour marcher dans les rochers.", PlaceType.DESERT, R.drawable.day_29),
    Place(30, "Testour", "Une ville andalouse et son minaret à l'horloge inversée",
        "Testour, dans le nord-ouest du pays, a été fondée au XVIIe siècle par des Andalous venus d'Espagne. On y retrouve leur style dans les maisons, les ruelles et la Grande Mosquée, dont le minaret décoré de carreaux porte une horloge célèbre : ses aiguilles tournent dans le sens inverse. La ville accueille aussi le Festival international du Malouf, consacré à la musique andalouse.",
        "Promenez-vous dans les ruelles autour de la Grande Mosquée et regardez bien l'horloge du minaret.", PlaceType.MEDINA, R.drawable.day_30),
)

// =====================================================================
// ACTIVITY
// =====================================================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaysOfTunisiaTheme {
                DaysOfTunisiaApp()
            }
        }
    }
}

// =====================================================================
// UI
// =====================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysOfTunisiaApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("30 Days of Tunisia", style = MaterialTheme.typography.titleLarge)
                        Text("30 jours pour découvrir la Tunisie", style = MaterialTheme.typography.labelLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp, end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(places, key = { it.day }) { place ->
                PlaceCard(place)
            }
        }
    }
}

@Composable
fun PlaceCard(place: Place, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "arrow")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            // Ochre strip at the top of the card
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = place.day.toString(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = place.type.label.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = place.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = place.summary,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Réduire" else "Afficher plus",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            Image(
                painter = painterResource(place.image),
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = place.details,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Conseil pratique",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = place.tip,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}