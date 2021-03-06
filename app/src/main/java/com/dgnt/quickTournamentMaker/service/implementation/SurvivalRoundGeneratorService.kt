package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class SurvivalRoundGeneratorService(private val participantService: IParticipantService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (RoundGroup) -> String, defaultRoundTitleFunc: (Round) -> String, defaultMatchUpTitleFunc: (MatchUp) -> String): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)
        val totalParticipants = orderedParticipants.size / 2
        val rounds = (0 until totalParticipants - 1).map { roundIndex ->

            when (roundIndex) {
                0 -> round1
                else -> Round(0, roundIndex,
                    (1..totalParticipants).map {
                        MatchUp(0, roundIndex, it, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                            .apply { title = defaultMatchUpTitleFunc(this) }
                    }
                ).apply {
                    title = defaultRoundTitleFunc(this)
                }
            }
        }

        return listOf(RoundGroup(0, rounds).apply { title = defaultRoundGroupTitleFunc(this) })
    }
}