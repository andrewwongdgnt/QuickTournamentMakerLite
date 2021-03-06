package com.dgnt.quickTournamentMaker

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.data.TournamentTypeServices
import com.dgnt.quickTournamentMaker.service.implementation.TournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

class TournamentBuilderServiceTest {

    private val mockEliminationRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockDoubleEliminationRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockRoundRobinRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockSwissRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)
    private val mockSurvivalRoundGeneratorService: IRoundGeneratorService = PowerMockito.mock(IRoundGeneratorService::class.java)

    private val mockEliminationRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockDoubleEliminationRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockRoundRobinRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockSwissRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)
    private val mockSurvivalRoundUpdateService: IRoundUpdateService = PowerMockito.mock(IRoundUpdateService::class.java)

    private val mockEliminationRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockDoubleEliminationRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockRoundRobinRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockSwissRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)
    private val mockSurvivalRankingService: IRankingService = PowerMockito.mock(IRankingService::class.java)

    private val mockEliminationMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockDoubleEliminationMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockRoundRobinMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockSwissMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)
    private val mockSurvivalMatchUpStatusTransformService: IMatchUpStatusTransformService = PowerMockito.mock(IMatchUpStatusTransformService::class.java)


    private val sut = TournamentBuilderService(
        mapOf(
            TournamentType.ELIMINATION to TournamentTypeServices(
                roundGeneratorService = mockEliminationRoundGeneratorService,
                roundUpdateService = mockEliminationRoundUpdateService,
                rankingService = mockEliminationRankingService,
                matchUpStatusTransformService = mockEliminationMatchUpStatusTransformService
            ),
            TournamentType.DOUBLE_ELIMINATION to TournamentTypeServices(
                roundGeneratorService = mockDoubleEliminationRoundGeneratorService,
                roundUpdateService = mockDoubleEliminationRoundUpdateService,
                rankingService = mockDoubleEliminationRankingService,
                matchUpStatusTransformService = mockDoubleEliminationMatchUpStatusTransformService
            ),
            TournamentType.ROUND_ROBIN to TournamentTypeServices(
                roundGeneratorService = mockRoundRobinRoundGeneratorService,
                roundUpdateService = mockRoundRobinRoundUpdateService,
                rankingService = mockRoundRobinRankingService,
                matchUpStatusTransformService = mockRoundRobinMatchUpStatusTransformService
            ),
            TournamentType.SWISS to TournamentTypeServices(
                roundGeneratorService = mockSwissRoundGeneratorService,
                roundUpdateService = mockSwissRoundUpdateService,
                rankingService = mockSwissRankingService,
                matchUpStatusTransformService = mockSwissMatchUpStatusTransformService
            ),
            TournamentType.SURVIVAL to TournamentTypeServices(
                roundGeneratorService = mockSurvivalRoundGeneratorService,
                roundUpdateService = mockSurvivalRoundUpdateService,
                rankingService = mockSurvivalRankingService,
                matchUpStatusTransformService = mockSurvivalMatchUpStatusTransformService
            )
        )
    )

    private val eliminationTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val doubleEliminationTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.DOUBLE_ELIMINATION, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val roundRobinTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.ROUND_ROBIN, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val swissTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.SWISS, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    private val survivalTournamentInformation = TournamentInformation("title", "description", listOf(Data.ANDREW, Data.DEMON, Data.DGNT), TournamentType.SURVIVAL, SeedType.CUSTOM, RankPriorityConfig.DEFAULT, Calendar.getInstance().time)

    @Before
    fun setUp() {

    }

    @Test
    fun testEliminationServiceCall() {
        sut.build(eliminationTournamentInformation, listOf()).also {
            Mockito.verify(mockEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
        }
    }

    @Test
    fun testDoubleEliminationServiceCall() {
        sut.build(doubleEliminationTournamentInformation, listOf()).also {
            Mockito.verify(mockDoubleEliminationRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
        }
    }

    @Test
    fun testRoundRobinServiceCall() {
        sut.build(roundRobinTournamentInformation, listOf()).also {
            Mockito.verify(mockRoundRobinRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
        }
    }

    @Test
    fun testSwissServiceCall() {
        sut.build(swissTournamentInformation, listOf()).also {
            Mockito.verify(mockSwissRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
        }
    }

    @Test
    fun testSurvivalServiceCall() {
        sut.build(survivalTournamentInformation, listOf()).also {
            Mockito.verify(mockSurvivalRoundGeneratorService, Mockito.times(1)).build(MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
        }
    }

    @Test
    fun testEquality() {
        sut.build(eliminationTournamentInformation, listOf()).run {
            Assert.assertEquals(mockEliminationRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockEliminationRankingService, rankingService)
            Assert.assertEquals(mockEliminationMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(doubleEliminationTournamentInformation, listOf()).run {
            Assert.assertEquals(mockDoubleEliminationRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockDoubleEliminationRankingService, rankingService)
            Assert.assertEquals(mockDoubleEliminationMatchUpStatusTransformService, matchUpStatusTransformService)
        }
        sut.build(roundRobinTournamentInformation, listOf()).run {
            Assert.assertEquals(mockRoundRobinRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockRoundRobinRankingService, rankingService)
            Assert.assertEquals(mockRoundRobinMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(swissTournamentInformation, listOf()).run {
            Assert.assertEquals(mockSwissRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockSwissRankingService, rankingService)
            Assert.assertEquals(mockSwissMatchUpStatusTransformService, matchUpStatusTransformService)
        }

        sut.build(survivalTournamentInformation, listOf()).run {
            Assert.assertEquals(mockSurvivalRoundUpdateService, roundUpdateService)
            Assert.assertEquals(mockSurvivalRankingService, rankingService)
            Assert.assertEquals(mockSurvivalMatchUpStatusTransformService, matchUpStatusTransformService)
        }

    }




}