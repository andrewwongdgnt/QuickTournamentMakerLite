package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService

class ParticipantService : IParticipantService {
    override fun createRound(participants: List<Participant>): Round = Round(participants.zipWithNext().filterIndexed { index, _ -> index % 2 == 0 }.map { MatchUp(it.first, it.second) })
}