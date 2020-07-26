package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusResolverService

class ByeStatusResolverService : IMatchUpStatusResolverService{

    override fun resolve(roundGroups: List<RoundGroup>, vararg roundGroupRoundPair: Pair<Int, Int>) {
        roundGroupRoundPair.forEach {
            roundGroups[it.first].rounds[it.second].matchUps.filter{m->m.participant1== Participant.BYE_PARTICIPANT || m.participant2 == Participant.BYE_PARTICIPANT}.forEach {
                    m->m.status = if (m.participant1== Participant.BYE_PARTICIPANT) MatchUpStatus.P2_WINNER else MatchUpStatus.P1_WINNER
            }
        }
    }

}