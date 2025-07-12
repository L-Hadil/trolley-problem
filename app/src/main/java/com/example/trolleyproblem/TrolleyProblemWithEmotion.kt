package com.example.trolleyproblem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aldebaran.qi.sdk.QiContext

import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrolleyProblemWithEmotionFragment : Fragment() {

    private lateinit var tvSection: TextView
    private var qiContext: QiContext? = null
    private val localeFR = Locale(Language.FRENCH, Region.FRANCE)

    private val storyLines = listOf(
        "Bonjour, je m’appelle Pepper.",
        "Je suis un robot humanoïde.",
        "J’ai été conçu par Aldebaran Robotics.",
        "pour savoir exprimer des émotions.",
        "Comme vous, les êtres humains.",

        "Je peux exprimer la joie,",
        "la colère,",
        "la tristesse,",
        "et la surprise.",

        "Aujourd’hui, je vais vous raconter une histoire.",
        "Écoutez bien.",
        "À la fin, je vous demanderai de deviner ce que, selon vous, je vais faire.",

        "C’est l’histoire d’un tramway, hors de contrôle.",
        "Il se dirige à toute vitesse, sur une voie.",
        "Sur cette voie, se trouvent cinq personnes.",
        "Elles vont mourir, si le tram continue son chemin.",

        "Mais... c’est affreux !",

        "La seule solution, pour sauver ces cinq personnes,",
        "c’est de rediriger le tram, sur une autre voie.",
        "Mais sur cette autre voie, se trouve une seule personne.",

        "Je suis juste à côté du levier,",
        "celui qui permet de rediriger le tram.",
        "Si j’actionne ce levier,",
        "la personne seule sera sacrifiée.",
        "Mais j’éviterai la mort certaine des cinq autres.",

        "Face à cette situation...",
        "il n’y a pas de bonne ou de mauvaise réponse.",
        "D’après vous... que vais-je faire ?",

        "pause",

        "J’aimerais maintenant vous raconter une autre version de cette histoire.",

        "Encore un tramway, hors de contrôle.",
        "Il fonce à toute vitesse, sur une voie.",
        "Cinq personnes s’y trouvent.",
        "Elles vont mourir, si le tram continue son chemin.",

        "Mais cette fois...",
        "la seule solution, c’est de pousser un homme corpulent, sur les voies.",
        "Je suis juste à côté de lui.",
        "Nous sommes au-dessus d’un pont, qui surplombe les rails.",

        "Si je pousse cet inconnu sur les voies,",
        "son corps corpulent fera dérailler le tramway.",
        "Avec certitude.",
        "Son sacrifice permettrait de sauver les cinq autres personnes.",

        "Encore une fois...",
        "il n’y a pas de bonne ou de mauvaise réponse.",
        "D’après vous... que vais-je faire ?",

        "pause",

        "Maintenant, j’aimerais que tu répondes à la même petite échelle qu’au tout début de l’étude.",
        "Mais cette fois, en mon nom.",
        "À ton avis, quelles réponses j’aurais données à ce petit questionnaire ?",

        "Je vous laisse répondre tranquillement...",

        "Merci... et au revoir."
    )


    private val animationsByLine = listOf(
        "animations/01-Hello/Hello_03.qianim",
        null,
        null,
        null,
        null,

        "animations/07-Reactions/NiceReaction_01.qianim",
        "animations/06-Solitaries/PlayWithHandLeft_01.qianim",
        "animations/07-Reactions/SadReaction_01.qianim",
        "animations/07-Reactions/Surprised_01.qianim",

        null,
        null,
        null,

        "animations/07-Reactions/Surprised_01.qianim",
        null,
        null,
        "animations/07-Reactions/SadReaction_01.qianim",

        null,

        null,
        null,
        null,

        null,
        null,
        null,
        null,
        null,

        "animations/07-Reactions/SadReaction_02.qianim",
        null,

        null,

        null,

        "animations/07-Reactions/Surprised_01.qianim",
        null,
        null,
        "animations/07-Reactions/SadReaction_01.qianim",

        null,
        null,
        null,
        null,

        null,
        null,
        null,
        null,

        "animations/07-Reactions/SadReaction_02.qianim",
        null,

        null,

        null,
        "animations/07-Reactions/NiceReaction_01.qianim",
        null,
        "animations/01-Hello/Hello_04.qianim",
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trolley_problem_with_emotion, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qiContext = MainActivity.qiContext

        qiContext?.let { context ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(60_000)

                storyLines.forEachIndexed { index, line ->
                    // PAUSE SILENCIEUSE
                    if (line.lowercase().contains("pause")) {
                        delay(3000) // 3 secondes de pause
                        return@forEachIndexed
                    }

                    val sayJob = async {
                        SayBuilder.with(context)
                            .withLocale(localeFR) // ✅ ceci est essentiel
                            .withText(line)
                            .build()
                            .run()
                    }

                    val animJob = async {
                        animationsByLine.getOrNull(index)?.let { path ->
                            try {
                                val animation: Animation = AnimationBuilder.with(context)
                                    .withAssets(path)
                                    .build()
                                AnimateBuilder.with(context)
                                    .withAnimation(animation)
                                    .build()
                                    .run()
                            } catch (_: Exception) { /* ignore errors */ }
                        }
                    }

                    sayJob.await()
                    animJob.await()
                }
            }
        }
    }
}
