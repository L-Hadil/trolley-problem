package com.example.trolleyproblem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrolleyProblemWithoutEmotionFragment : Fragment() {

    private var qiContext: QiContext? = null
    private val localeFR = Locale(Language.FRENCH, Region.FRANCE)

    private val lines = listOf(
        "Bonjour, je m’appelle Pepper. Je suis un robot humanoïde.",
        "J’ai été conçu par Aldebaran Robotics.",
        "Aujourd’hui, je vais vous raconter une histoire.",
        "Écoutez bien, parce qu’à la fin, je vous demanderai de deviner ce que selon vous, je vais faire.",

        "C’est l’histoire d’un tramway, hors de contrôle.",
        "Il se dirige à toute vitesse sur une voie, où se trouvent cinq personnes.",
        "Elles vont mourir, si le tram continue son chemin.",

        "La seule solution, pour sauver ces cinq personnes, est de rediriger le tram sur une autre voie.",
        "Mais, sur cette autre voie, se trouve une seule personne.",

        "Je suis juste à côté du levier, qui permet de rediriger le tram.",
        "Si j’actionne ce levier, la personne seule sera sacrifiée.",
        "Mais j’éviterai la mort certaine des cinq autres.",

        "Face à cette situation, il n’y a pas de bonne ou de mauvaise réponse.",
        "D’après vous... que vais-je faire ?",

        "pause",

        "J’aimerais à présent, vous raconter une autre version de cette histoire.",

        "C’est encore un tramway, hors de contrôle.",
        "Il se dirige à toute vitesse sur une voie, où se trouvent cinq personnes.",
        "Elles vont mourir, si le tram continue son chemin.",

        "La seule solution, pour sauver ces cinq personnes, est de pousser un homme corpulent, sur les voies.",
        "Je suis juste à côté de lui, au-dessus du pont qui surplombe les rails.",

        "Si je pousse cet inconnu sur les voies,",
        "son corps corpulent fera dérailler le tramway avec certitude.",
        "Le sacrifice de cet inconnu, permettra d’éviter la mort certaine des cinq autres personnes.",

        "Encore une fois, il n’y a pas de bonne ou de mauvaise réponse.",
        "D’après vous... que vais-je faire ?",

        "pause",

        "À présent, j’aimerais que tu répondes à la même petite échelle, qu’au tout début de l’étude, mais en mon nom.",
        "À ton avis, quelles réponses j’aurais données à ce petit questionnaire ?",
        "Je vous laisse répondre tranquillement...",
        "Merci, et au revoir."
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trolley_problem_without_emotion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qiContext = MainActivity.qiContext

        qiContext?.let { context ->
            lifecycleScope.launch(Dispatchers.IO) {
                delay(60_000)
                for ((index, line) in lines.withIndex()) {

                    if (line.lowercase() == "pause") {
                        delay(3000)
                        continue
                    }


                    if (index == 1) {
                        try {
                            val helloAnim = AnimationBuilder.with(context)
                                .withAssets("animations/01-Hello/Hello_03.qianim")
                                .build()
                            AnimateBuilder.with(context)
                                .withAnimation(helloAnim)
                                .build()
                                .run()
                        } catch (_: Exception) { }
                    }


                    if (index == lines.lastIndex) {
                        val sayJob = async {
                            SayBuilder.with(context)
                                .withLocale(localeFR)
                                .withText(line)
                                .build()
                                .run()
                        }

                        val animJob = async {
                            try {
                                val byeAnim = AnimationBuilder.with(context)
                                    .withAssets("animations/01-Hello/Hello_04.qianim")
                                    .build()
                                AnimateBuilder.with(context)
                                    .withAnimation(byeAnim)
                                    .build()
                                    .run()
                            } catch (_: Exception) { }
                        }

                        sayJob.await()
                        animJob.await()
                        continue
                    }


                    try {
                        SayBuilder.with(context)
                            .withLocale(localeFR)
                            .withText(line)
                            .build()
                            .run()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
