package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService


class DoubleEliminationRoundUpdateService(private val roundUpdateService: IRoundUpdateService) : IRoundUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, rankConfig: IRankConfig) {

        val roundGroup = roundGroups[roundGroupIndex]

        //winner bracket
        if (roundGroupIndex == 0) {
            roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex)

            var currentRoundIndexWinnerBracket = roundIndex;
            var currentMatchUpIndexWinnerBracket = matchUpIndex;
            while (currentRoundIndexWinnerBracket < roundGroups[roundGroupIndex].rounds.size) {

                val currentRoundWinnerBracket = roundGroup.rounds[currentRoundIndexWinnerBracket]
                val currentMatchUpWinnerBracket = currentRoundWinnerBracket.matchUps[currentMatchUpIndexWinnerBracket]

                val currentRoundIndexLoserBracket = if (currentRoundIndexWinnerBracket == 0) 0 else currentRoundIndexWinnerBracket * 2 - 1
                val currentRoundLoserBracket = roundGroups[1].rounds[currentRoundIndexLoserBracket]

                val currentMatchUpIndexLoserBracket = when {
                    currentRoundIndexWinnerBracket == 0 -> currentMatchUpIndexWinnerBracket / 2
                    currentRoundIndexWinnerBracket % 2 == 1 -> currentRoundLoserBracket.matchUps.size - currentMatchUpIndexWinnerBracket - 1
                    else -> currentMatchUpIndexWinnerBracket
                }

                val currentMatchUpLoserBracket = currentRoundLoserBracket.matchUps[currentMatchUpIndexLoserBracket];

                //if this is not the first round, then we always move the loser of the winner bracket match up to the participant 1 position of the corresponding loser bracket match up
                //otherwise, we alternate

                val newParticipant = when (currentMatchUpWinnerBracket.status) {
                    MatchUpStatus.P1_WINNER -> currentMatchUpWinnerBracket.participant2;
                    MatchUpStatus.P2_WINNER -> currentMatchUpWinnerBracket.participant1;
                    else -> Participant.NULL_PARTICIPANT;
                }

                if (currentRoundIndexWinnerBracket != 0 || currentMatchUpIndexWinnerBracket % 2 == 0) {
                    if (currentMatchUpLoserBracket.participant1 != newParticipant)
                        currentMatchUpLoserBracket.participant1 = newParticipant;
                    else
                        break;
                } else {
                    if (currentMatchUpLoserBracket.participant2 != newParticipant)
                        currentMatchUpLoserBracket.participant2 = newParticipant;
                    else
                        break;
                }

                update(roundGroups, 1, currentRoundIndexLoserBracket, currentMatchUpIndexLoserBracket);

                currentRoundIndexWinnerBracket++
                currentMatchUpIndexWinnerBracket /= 2
            }
            if (currentRoundIndexWinnerBracket == roundGroup.rounds.size) {
                update(roundGroups, 2, 0, 0);
            }
        }

        //loser bracket
        else if (roundGroupIndex == 1) {

            var currentRoundIndex = roundIndex;
            var currentMatchUpIndex = matchUpIndex;
            var continueLoop = true;
            while (continueLoop && currentRoundIndex < roundGroups[roundGroupIndex].rounds.lastIndex) {

                val currentRound = roundGroup.rounds[currentRoundIndex]

                val currentMatchUp = currentRound.matchUps[currentMatchUpIndex]

                val futureRoundIndex = currentRoundIndex + 1
                val futureRound = roundGroup.rounds[futureRoundIndex]

                val futureMatchUpIndex = if (futureRoundIndex % 2 == 0) currentMatchUpIndex / 2 else currentMatchUpIndex
                val futureMatchUp = futureRound.matchUps[futureMatchUpIndex]

                val updateFutureMatchUp: (Participant) -> Boolean = {
                    val differentParticipant: Boolean
                    if (currentRoundIndex % 2 == 0 || currentMatchUpIndex % 2 == 1) {
                        differentParticipant = futureMatchUp.participant1.key != it.key
                        futureMatchUp.participant2 = it
                    } else {
                        differentParticipant = futureMatchUp.participant2.key != it.key
                        futureMatchUp.participant1 = it
                    }

                    differentParticipant
                }

                continueLoop = updateFutureMatchUp(
                    when (currentMatchUp.status) {
                        MatchUpStatus.P1_WINNER -> currentMatchUp.participant1
                        MatchUpStatus.P2_WINNER -> currentMatchUp.participant2
                        else -> Participant.NULL_PARTICIPANT
                    }
                )
                currentRoundIndex = futureRoundIndex
                currentMatchUpIndex = futureMatchUpIndex
            }
            if (currentRoundIndex == roundGroup.rounds.lastIndex) {
                update(roundGroups, 2, 0, 0);
            }
        }

        //final bracket
        else if (roundGroupIndex == 2) {
            val matchUp1FinalBracket = roundGroup.rounds[0].matchUps.first()
            val matchUp2FinalBracket = roundGroup.rounds[1].matchUps.first()

            val lastMatchUpWinnerBracket = roundGroups[0].rounds.last().matchUps.first();
            matchUp1FinalBracket.participant1 = when (lastMatchUpWinnerBracket.status) {
                MatchUpStatus.P1_WINNER -> lastMatchUpWinnerBracket.participant1
                MatchUpStatus.P2_WINNER -> lastMatchUpWinnerBracket.participant2
                else -> Participant.NULL_PARTICIPANT
            }

            val lastMatchUpLoserBracket = roundGroups[1].rounds.last().matchUps.first();
            matchUp1FinalBracket.participant2 = when (lastMatchUpLoserBracket.status) {
                MatchUpStatus.P1_WINNER -> lastMatchUpLoserBracket.participant1
                MatchUpStatus.P2_WINNER -> lastMatchUpLoserBracket.participant2
                else -> Participant.NULL_PARTICIPANT
            }

            when (matchUp1FinalBracket.status) {
                MatchUpStatus.P1_WINNER, MatchUpStatus.DEFAULT -> {
                    matchUp2FinalBracket.participant1 = Participant.NULL_PARTICIPANT
                    matchUp2FinalBracket.participant2 = Participant.NULL_PARTICIPANT
                }
                else -> {
                    matchUp2FinalBracket.participant1 = matchUp1FinalBracket.participant1
                    matchUp2FinalBracket.participant2 = matchUp1FinalBracket.participant2
                }
            }
        }
    }
}