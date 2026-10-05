package com.example.data.repository

import com.example.data.engine.BandScoreCalculator
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class IeltsRepository private constructor() {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "usr_01",
            name = "Haris Mahmood",
            email = "haris.ielts@academy.db",
            role = UserRole.STUDENT,
            ieltsType = IeltsType.ACADEMIC,
            currentBand = 6.5,
            targetBand = 7.5,
            examDateDaysLeft = 42,
            dailyGoalMinutes = 60,
            streakDays = 7,
            studyHoursTotal = 24.5,
            xp = 1850,
            level = 4,
            listeningBand = 7.5,
            readingBand = 7.0,
            writingBand = 6.5,
            speakingBand = 6.5
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _dailyMissions = MutableStateFlow(
        listOf(
            DailyMissionItem("m1", "Listening Practice (Section 2)", IeltsModuleType.LISTENING, 15, true),
            DailyMissionItem("m2", "High-Band Vocabulary (10 Words)", IeltsModuleType.VOCABULARY, 10, true),
            DailyMissionItem("m3", "Academic Reading Passage 1", IeltsModuleType.READING, 20, true),
            DailyMissionItem("m4", "Writing Task 2 Essay Draft", IeltsModuleType.WRITING, 40, false),
            DailyMissionItem("m5", "Speaking Cue Card Recording", IeltsModuleType.SPEAKING, 15, false)
        )
    )
    val dailyMissions: StateFlow<List<DailyMissionItem>> = _dailyMissions.asStateFlow()

    private val _writingSubmissions = MutableStateFlow(
        listOf(
            WritingSubmission(
                id = "w_sub_101",
                studentName = "Haris Mahmood",
                promptId = "p_task2_01",
                taskType = "Task 2",
                promptTitle = "AI in Academic Higher Education",
                essayText = "In contemporary society, the integration of artificial intelligence into pedagogical frameworks has ignited contentious discourse. While proponents postulate that adaptive algorithms foster personalized learning trajectories, critics assert that overreliance on automated systems could atrophy critical cognitive capabilities. In my opinion, AI serves as an indispensable catalyst for academic innovation, provided rigorous academic integrity paradigms are maintained.\n\nOn the one hand, neural synthesis engines furnish bespoke didactic material tailored to individual comprehension rates. For instance, struggling learners receive granular scaffolding, thereby bridging the achievement divide without encumbering classroom instructors. Furthermore, automated evaluative mechanisms afford instantaneous diagnostic feedback, expediting revision cycles significantly.\n\nConversely, unfettered reliance poses palpable perils to scholarly rigor. If undergraduates delegate heuristic inquiry to generative models, analytical synthesis will inevitably degenerate into superficial regurgitation. Therefore, institutional stakeholders must implement comprehensive ethical guidelines rather than outright proscriptions.\n\nIn conclusion, rather than thwarting technological disruption, universities ought to harness artificial intelligence to augment intellectual curiosity while safeguarding the paramount sanctity of original scholarly analysis.",
                wordCount = 274,
                submittedAt = "Yesterday at 18:30",
                isEvaluated = true,
                taskResponseScore = 7.0,
                coherenceCohesionScore = 6.5,
                lexicalResourceScore = 7.5,
                grammarScore = 6.5,
                overallBand = 7.0,
                teacherFeedback = "Strong thesis statement and sophisticated academic vocabulary ('pedagogical frameworks', 'heuristic inquiry'). Cohesion needs tighter topic transition in Paragraph 3.",
                teacherStrengths = "Exceptional lexical density; clear overarching position sustained throughout.",
                teacherWeaknesses = "Complex sentence variety could be polished; some punctuation slips in conditional clauses.",
                teacherCorrections = "Replace ' bridges the achievement divide without encumbering' with 'mitigates disparities without overburdening'."
            ),
            WritingSubmission(
                id = "w_sub_102",
                studentName = "Zainab Fatima",
                promptId = "p_task1_01",
                taskType = "Task 1",
                promptTitle = "Global Renewable Energy Consumption (2010-2025)",
                essayText = "The provided bar chart delineates the volumetric distribution of renewable power generation across solar, wind, and hydroelectric sources between 2010 and 2025 in terawatt-hours.\n\nOverall, it is readily apparent that total renewable output experienced an upward trajectory over the fifteen-year period, with solar power manifesting the most pronounced proportional escalation while hydro remained the most stable baseline contributor throughout.\n\nIn 2010, hydroelectric power dominated with approximately 450 TWh, surpassing wind at 210 TWh and solar which registered a nominal 35 TWh. Over the subsequent decade, solar generation exhibited exponential expansion, culminating at 580 TWh in 2025. Concurrently, wind escalated steadily to 520 TWh. In stark contrast, hydroelectric energy experienced moderate fluctuations, plateauing at roughly 490 TWh.",
                wordCount = 162,
                submittedAt = "Today at 10:15",
                isEvaluated = false
            )
        )
    )
    val writingSubmissions: StateFlow<List<WritingSubmission>> = _writingSubmissions.asStateFlow()

    private val _speakingSubmissions = MutableStateFlow(
        listOf(
            SpeakingSubmission(
                id = "spk_sub_01",
                studentName = "Haris Mahmood",
                cueCardTitle = "Describe a Futuristic Technology You Would Like to Use",
                part = 2,
                durationSeconds = 118,
                recordedDate = "2 days ago",
                isEvaluated = true,
                fluencyScore = 6.5,
                lexicalScore = 7.0,
                grammarScore = 6.5,
                pronunciationScore = 6.5,
                overallBand = 6.5,
                teacherFeedback = "Fluent speech with minimal hesitation. Try to vary intonation on transition markers to reach Band 7.5 in Pronunciation."
            )
        )
    )
    val speakingSubmissions: StateFlow<List<SpeakingSubmission>> = _speakingSubmissions.asStateFlow()

    private val _vocabList = MutableStateFlow(
        listOf(
            VocabWord(
                id = "v1",
                word = "Ubiquitous",
                phonetic = "/juːˈbɪk.wɪ.təs/",
                partOfSpeech = "Adjective",
                definition = "Present, appearing, or found everywhere.",
                sampleSentence = "Smartphones have become ubiquitous in modern academic environments.",
                synonyms = listOf("Omnipresent", "Pervasive", "Universal"),
                collocations = listOf("ubiquitous presence", "become ubiquitous", "virtually ubiquitous"),
                ieltsBand = 8.5,
                topic = "Technology & Society"
            ),
            VocabWord(
                id = "v2",
                word = "Exacerbate",
                phonetic = "/ɪɡˈzæs.ə.beɪt/",
                partOfSpeech = "Verb",
                definition = "Make a problem, bad situation, or negative feeling worse.",
                sampleSentence = "Uncontrolled urban sprawl directly exacerbates ambient air pollution in metropolitan hubs.",
                synonyms = listOf("Aggravate", "Worsen", "Inflame"),
                collocations = listOf("exacerbate tensions", "exacerbate the problem", "serve to exacerbate"),
                ieltsBand = 8.0,
                topic = "Environment & Urban Planning"
            ),
            VocabWord(
                id = "v3",
                word = "Pernicious",
                phonetic = "/pəˈnɪʃ.əs/",
                partOfSpeech = "Adjective",
                definition = "Having a harmful effect, especially in a gradual or subtle way.",
                sampleSentence = "Sedentary leisure habits can have a pernicious influence on adolescent cognitive stamina.",
                synonyms = listOf("Detrimental", "Inimical", "Noxious"),
                collocations = listOf("pernicious effect", "pernicious influence", "pernicious habit"),
                ieltsBand = 8.5,
                topic = "Health & Well-being"
            ),
            VocabWord(
                id = "v4",
                word = "Catalyst",
                phonetic = "/ˈkæt.əl.ɪst/",
                partOfSpeech = "Noun",
                definition = "A person or thing that precipitates an event or accelerates change.",
                sampleSentence = "Targeted municipal subsidies acted as a primary catalyst for renewable infrastructure adoption.",
                synonyms = listOf("Impetus", "Stimulus", "Spark"),
                collocations = listOf("act as a catalyst", "catalyst for change", "major catalyst"),
                ieltsBand = 7.5,
                topic = "Economy & Governance"
            ),
            VocabWord(
                id = "v5",
                word = "Substantiate",
                phonetic = "/səbˈstæn.ʃi.eɪt/",
                partOfSpeech = "Verb",
                definition = "Provide evidence to support or prove the truth of something.",
                sampleSentence = "Candidates must substantiate their claims with empirical research to secure Band 8+ in Task Achievement.",
                synonyms = listOf("Corroborate", "Validate", "Verify"),
                collocations = listOf("substantiate claims", "substantiate findings", "substantiate evidence"),
                ieltsBand = 8.0,
                topic = "Academic Writing"
            ),
            VocabWord(
                id = "v6",
                word = "Mitigate",
                phonetic = "/ˈmɪt.ɪ.ɡeɪt/",
                partOfSpeech = "Verb",
                definition = "Make less severe, serious, or painful.",
                sampleSentence = "Government incentives for electric vehicles have helped mitigate carbon emissions in industrial cities.",
                synonyms = listOf("Alleviate", "Attenuate", "Diminish"),
                collocations = listOf("mitigate risk", "mitigate the impact", "mitigate circumstances"),
                ieltsBand = 8.0,
                topic = "Environment & Society"
            ),
            VocabWord(
                id = "v7",
                word = "Proliferation",
                phonetic = "/prəˌlɪf.əˈreɪ.ʃən/",
                partOfSpeech = "Noun",
                definition = "Rapid increase in numbers or amount.",
                sampleSentence = "The sudden proliferation of automated digital algorithms has revolutionized computational pedagogy.",
                synonyms = listOf("Escalation", "Expansion", "Multiplication"),
                collocations = listOf("rapid proliferation", "proliferation of devices", "widespread proliferation"),
                ieltsBand = 8.5,
                topic = "Technology & Science"
            ),
            VocabWord(
                id = "v8",
                word = "Paradigm",
                phonetic = "/ˈpær.ə.daɪm/",
                partOfSpeech = "Noun",
                definition = "A typical example, pattern, or overarching model.",
                sampleSentence = "The transition toward renewable decentralized grids signifies a major paradigm shift in energy planning.",
                synonyms = listOf("Archetype", "Benchmark", "Standard"),
                collocations = listOf("paradigm shift", "dominant paradigm", "new paradigm"),
                ieltsBand = 8.0,
                topic = "Science & Governance"
            ),
            VocabWord(
                id = "v9",
                word = "Nuanced",
                phonetic = "/ˈnjuː.ɑːnst/",
                partOfSpeech = "Adjective",
                definition = "Characterized by subtle distinctions or multifaceted shades of meaning.",
                sampleSentence = "Examiners look for a nuanced appraisal of socioeconomic trade-offs rather than simplistic generalizations.",
                synonyms = listOf("Subtle", "Discerning", "Refined"),
                collocations = listOf("nuanced understanding", "nuanced approach", "nuanced perspective"),
                ieltsBand = 8.5,
                topic = "Academic Writing"
            ),
            VocabWord(
                id = "v10",
                word = "Discourse",
                phonetic = "/ˈdɪs.kɔːs/",
                partOfSpeech = "Noun",
                definition = "Written or spoken communication or intellectual debate.",
                sampleSentence = "Critical discourse surrounding academic integrity must evolve in parallel with neural generative models.",
                synonyms = listOf("Dialogue", "Discussion", "Exchange"),
                collocations = listOf("public discourse", "scholarly discourse", "frame the discourse"),
                ieltsBand = 8.0,
                topic = "Education & Philosophy"
            )
        )
    )
    val vocabList: StateFlow<List<VocabWord>> = _vocabList.asStateFlow()

    private val _grammarTopics = MutableStateFlow(
        listOf(
            GrammarTopic(
                id = "g1",
                title = "Passive Voice in Academic Reports",
                tag = "Task 1 & Task 2",
                bandImpact = "Boosts Grammatical Range & Academic Neutrality",
                explanation = "In IELTS Academic writing, using the passive voice allows candidates to emphasize the process, findings, or data rather than the human actor. It gives your essay an objective, scholarly tone.",
                keyRules = listOf(
                    "Form with: Subject + appropriate form of 'to be' + Past Participle (V3).",
                    "Do not mention the agent ('by people') unless the agent is essential to the meaning.",
                    "Use when describing graphs, manufacturing cycles, or scientific processes."
                ),
                examples = listOf(
                    "Active (Informal)" to "Researchers collected the water samples over six months.",
                    "Passive (Academic)" to "Water samples were systematically collected over a six-month duration.",
                    "Process Passive" to "The raw components are transported to the smelting facility where thermal treatment is applied."
                ),
                quiz = listOf(
                    GrammarQuestion(
                        id = "qg1",
                        prompt = "Select the most appropriate academic passive phrasing:",
                        options = listOf(
                            "They increased the taxes significantly in 2022.",
                            "Taxes were increased significantly in 2022.",
                            "Significant taxes got increased by them in 2022."
                        ),
                        correctIndex = 1,
                        explanation = "Option 2 is an objective academic passive construction suitable for IELTS Academic Task 1 & 2."
                    )
                )
            ),
            GrammarTopic(
                id = "g2",
                title = "Inversion & Advanced Conditionals",
                tag = "Band 8.0+ Structures",
                bandImpact = "Unlocks Top Band Grammatical Range",
                explanation = "Inversion and conditional ellipsis show sophisticated grammatical control. Examiners reward these when used accurately to emphasize contrast or hypothetical consequences.",
                keyRules = listOf(
                    "'Had + Subject + V3' replaces 'If + Subject + had + V3' (e.g., 'Had governments acted earlier...').",
                    "'Were + Subject + to-infinitive' replaces hypothetical conditionals.",
                    "Inverted negative adverbs: 'Not only did the policy fail, but it also...'"
                ),
                examples = listOf(
                    "Standard Conditional" to "If authorities had allocated more funds, the transit system would have expanded.",
                    "Advanced Inversion" to "Had authorities allocated more funds, the public transit network would have expanded markedly.",
                    "Negative Adverbial" to "Seldom has a technological shift influenced pedagogical structures so dramatically."
                ),
                quiz = listOf(
                    GrammarQuestion(
                        id = "qg2",
                        prompt = "Which inverted sentence correctly replaces 'If the policy were to be implemented'?",
                        options = listOf(
                            "Were the policy to be implemented, significant ecological dividends would ensue.",
                            "Was the policy implemented, dividends would ensue.",
                            "Had the policy to be implemented, dividends would ensue."
                        ),
                        correctIndex = 0,
                        explanation = "'Were + subject + to be + past participle' is the accurate formal inverted subjunctive conditional."
                    )
                )
            ),
            GrammarTopic(
                id = "g3",
                title = "Cohesive Complex Sentences",
                tag = "Coherence & Grammar",
                bandImpact = "Essential for Band 7.0+",
                explanation = "High-scoring IELTS candidates combine compound sentences with subordinate adverbial clauses (concession, cause, contrast) seamlessly.",
                keyRules = listOf(
                    "Use concession markers: 'Although', 'While', 'Notwithstanding the fact that'.",
                    "Avoid overusing basic 'However' and 'Furthermore' at the start of every sentence; embed adverbials mid-sentence.",
                    "Maintain parallel grammatical structure across paired conjunctions."
                ),
                examples = listOf(
                    "Basic Linking" to "The population grew. As a result, housing costs rose.",
                    "Synthesized Complex" to "As metropolitan populations surged, housing expenditures escalated commensurately, precipitating an affordability crisis."
                ),
                quiz = listOf(
                    GrammarQuestion(
                        id = "qg3",
                        prompt = "Choose the sentence demonstrating superior academic cohesion:",
                        options = listOf(
                            "Electric cars are clean. But they are expensive so people do not buy them.",
                            "While electric propulsion yields distinct environmental benefits, high capital expenditure continues to deter prospective buyers.",
                            "Electric vehicles are good, because pollution is stopped, however price is high."
                        ),
                        correctIndex = 1,
                        explanation = "A subordinate clause with 'While' creates nuanced evaluation and sophisticated sentence variety."
                    )
                )
            ),
            GrammarTopic(
                id = "g4",
                title = "Relative Clauses for Syntactic Synthesis",
                tag = "Task 1 & Task 2",
                bandImpact = "Eliminates repetitive sentences; demonstrates Band 8+ synthesis",
                explanation = "Defining and non-defining relative clauses permit candidates to bundle evidence and evaluation compactly without stringing choppy short sentences together.",
                keyRules = listOf(
                    "Non-defining clauses require commas and provide supplementary descriptive commentary: '..., which escalated by 24%,'",
                    "Never use 'that' in non-defining clauses; use 'which' for things and 'who' for people.",
                    "Preposition + relative pronoun ('in which', 'to whom', 'whereby') adds formal precision."
                ),
                examples = listOf(
                    "Choppy Baseline" to "Solar power expanded. It reached 580 TWh. This made it the dominant clean source.",
                    "Synthesized Academic" to "Solar generation expanded exponentially to 580 TWh, which solidified its position as the predominant clean energy contributor."
                ),
                quiz = listOf(
                    GrammarQuestion(
                        id = "qg4",
                        prompt = "Select the sentence with accurate non-defining relative clause punctuation:",
                        options = listOf(
                            "Hydroelectric dams which provide baseload stability remained constant.",
                            "Hydroelectric facilities, which supply baseload grid stability, remained remarkably constant throughout the decade.",
                            "Hydroelectric facilities that supply baseload grid stability, remained constant."
                        ),
                        correctIndex = 1,
                        explanation = "Non-defining relative clauses take surrounding commas and utilize 'which' for inanimate facilities."
                    )
                )
            ),
            GrammarTopic(
                id = "g5",
                title = "Academic Hedging & Epistemic Modals",
                tag = "Academic Tone (Task 2)",
                bandImpact = "Avoids absolute overgeneralization penalties",
                explanation = "IELTS examiners penalize sweeping absolute claims like 'All children become lazy due to computers'. Academic writers use cautious modal hedging ('tends to', 'could potentially', 'it is plausible that').",
                keyRules = listOf(
                    "Use tentative modal verbs: 'may', 'might', 'could' rather than 'will' or 'is'.",
                    "Employ epistemic adverbs: 'arguably', 'predominantly', 'conceivably'.",
                    "Distance authorial assertions: 'Evidence suggests that...' rather than 'It is a definite fact that...'"
                ),
                examples = listOf(
                    "Overgeneralized (Band 5)" to "Social media destroys attention spans of every young person.",
                    "Hedging Academic (Band 8+)" to "Prolonged exposure to algorithmic media may conceivably diminish sustained attention spans among adolescent cohorts."
                ),
                quiz = listOf(
                    GrammarQuestion(
                        id = "qg5",
                        prompt = "Which sentence best demonstrates cautious academic hedging?",
                        options = listOf(
                            "Automation always causes massive unemployment everywhere.",
                            "Technological automation will certainly ruin workers' lives.",
                            "Rapid industrial automation could potentially precipitate labor market dislocations in traditional manufacturing sectors."
                        ),
                        correctIndex = 2,
                        explanation = "'could potentially precipitate' employs accurate modal hedging suitable for high-band academic tone."
                    )
                )
            )
        )
    )
    val grammarTopics: StateFlow<List<GrammarTopic>> = _grammarTopics.asStateFlow()

    // Pre-seeded Full Mock Tests
    private val _mockTests = MutableStateFlow(
        listOf(
            MockTest(
                id = "test_ac_01",
                title = "Academic Full Mock Test #1",
                ieltsType = IeltsType.ACADEMIC,
                difficulty = "Standard Exam Standard",
                durationMinutes = 160,
                totalQuestions = 40,
                moduleType = IeltsModuleType.LISTENING,
                sections = listOf(
                    TestSection(
                        sectionNumber = 1,
                        title = "Section 1: Community Leisure Club Registration",
                        instructions = "Complete the notes below. Write NO MORE THAN TWO WORDS AND/OR A NUMBER for each answer.",
                        audioTitle = "Track 01: Dialogue between Clerk and Applicant",
                        audioScript = "Clerk: Good morning, Metro Community Sports Complex. How can I assist you today?\nCaller: Hello, I'm calling to inquire about the seasonal club membership. My name is Alex Turner.\nClerk: Pleased to meet you, Alex. Can I confirm your contact address?\nCaller: Yes, it's 42 Highfield Crescent, Northwood.\nClerk: And your primary sport of interest?\nCaller: Primarily badminton, though I also intend to use the heated pool on weekends.\nClerk: Excellent. Our annual swimming pass includes locker access for a registration fee of £45.",
                        questions = listOf(
                            TestQuestion(
                                id = "q1_1",
                                questionNumber = 1,
                                questionType = QuestionType.FORM_COMPLETION,
                                prompt = "Applicant Surname: Turner, Address: 42 _______ Crescent",
                                options = listOf(
                                    QuestionOption("a", "A", "Highfield"),
                                    QuestionOption("b", "B", "Highland"),
                                    QuestionOption("c", "C", "Hillcrest")
                                ),
                                correctAnswer = "Highfield",
                                explanation = "Alex stated: 'Yes, it's 42 Highfield Crescent, Northwood.'"
                            ),
                            TestQuestion(
                                id = "q1_2",
                                questionNumber = 2,
                                questionType = QuestionType.MULTIPLE_CHOICE,
                                prompt = "Primary sporting facility preferred on weekends:",
                                options = listOf(
                                    QuestionOption("a", "A", "Tennis courts"),
                                    QuestionOption("b", "B", "Heated pool"),
                                    QuestionOption("c", "C", "Gymnasium weights room")
                                ),
                                correctAnswer = "Heated pool",
                                explanation = "Alex stated he intends to use the heated pool on weekends."
                            ),
                            TestQuestion(
                                id = "q1_3",
                                questionNumber = 3,
                                questionType = QuestionType.FORM_COMPLETION,
                                prompt = "Locker registration fee: £_______",
                                options = listOf(
                                    QuestionOption("a", "A", "35"),
                                    QuestionOption("b", "B", "45"),
                                    QuestionOption("c", "C", "50")
                                ),
                                correctAnswer = "45",
                                explanation = "Clerk confirmed: 'Our annual swimming pass includes locker access for a registration fee of £45.'"
                            )
                        )
                    ),
                    TestSection(
                        sectionNumber = 2,
                        title = "Section 2: City Heritage Museum Expansion",
                        instructions = "Choose the correct letter A, B, or C.",
                        audioTitle = "Track 02: Museum Curator Presentation",
                        audioScript = "Curator: Welcome delegates. The newly commissioned East Wing houses artifacts dating back to the industrial revolution. Construction commenced in March 2021 after extensive community consultations.",
                        questions = listOf(
                            TestQuestion(
                                id = "q1_4",
                                questionNumber = 4,
                                questionType = QuestionType.MULTIPLE_CHOICE,
                                prompt = "The primary purpose of the East Wing expansion is to:",
                                options = listOf(
                                    QuestionOption("a", "A", "Display maritime trade exhibits"),
                                    QuestionOption("b", "B", "House industrial revolution artifacts"),
                                    QuestionOption("c", "C", "Provide restaurant facilities")
                                ),
                                correctAnswer = "House industrial revolution artifacts",
                                explanation = "The curator explicitly identified the East Wing as housing industrial revolution artifacts."
                            )
                        )
                    )
                ),
                isCompleted = true,
                bestEstimatedBand = 7.5
            ),
            MockTest(
                id = "test_ac_02",
                title = "Academic Reading Test #1",
                ieltsType = IeltsType.ACADEMIC,
                difficulty = "Challenging",
                durationMinutes = 60,
                totalQuestions = 40,
                moduleType = IeltsModuleType.READING,
                sections = listOf(
                    TestSection(
                        sectionNumber = 1,
                        title = "Reading Passage 1: Subterranean Biomass and Carbon Sinks",
                        instructions = "Do the following statements agree with the information given in Reading Passage 1? Write TRUE, FALSE, or NOT GIVEN.",
                        passageTitle = "The Hidden Reservoirs of Terrestrial Carbon",
                        passageText = "While atmospheric and oceanic carbon cycles receive dominant scientific scrutiny, subterranean ecosystems harbor approximately one-third of the planet's total living biomass. Deep underground microbial colonies, often termed the deep biosphere, subsist under extreme hydrostatic pressures and elevated thermal gradients.\n\nRecent genomic surveys conducted across deep borehole excavations reveal that these lithotrophic microorganisms do not rely on photosynthetic byproducts. Instead, they derive metabolic sustenance directly from the radiolytic decomposition of subterranean groundwater and geochemical reactions involving ferric basalt minerals. Consequently, geobiologists hypothesize that these subterranean biomes represent an exceptionally resilient biological repository, insulated from catastrophic surface climate perturbations.\n\nMoreover, the capacity of deep basaltic formations to chemically sequester injected carbon dioxide into permanent carbonate minerals within mere months presents a promising avenue for industrialized carbon capture initiatives. Pilot installations in volcanic basalt zones have demonstrated an efficiency rate exceeding 95% within two years of initial subterranean injection.",
                        questions = listOf(
                            TestQuestion(
                                id = "qr1_1",
                                questionNumber = 1,
                                questionType = QuestionType.TRUE_FALSE_NOT_GIVEN,
                                prompt = "Subterranean ecosystems contain roughly one-third of all living biomass on Earth.",
                                options = listOf(
                                    QuestionOption("t", "TRUE", "Statement matches the passage"),
                                    QuestionOption("f", "FALSE", "Statement contradicts the passage"),
                                    QuestionOption("ng", "NOT GIVEN", "Information is not provided in passage")
                                ),
                                correctAnswer = "TRUE",
                                explanation = "The passage states: 'subterranean ecosystems harbor approximately one-third of the planet's total living biomass.'"
                            ),
                            TestQuestion(
                                id = "qr1_2",
                                questionNumber = 2,
                                questionType = QuestionType.TRUE_FALSE_NOT_GIVEN,
                                prompt = "Microbial life in the deep biosphere relies primarily on solar photosynthesis for energy.",
                                options = listOf(
                                    QuestionOption("t", "TRUE", "Statement matches the passage"),
                                    QuestionOption("f", "FALSE", "Statement contradicts the passage"),
                                    QuestionOption("ng", "NOT GIVEN", "Information is not provided in passage")
                                ),
                                correctAnswer = "FALSE",
                                explanation = "The text says they 'do not rely on photosynthetic byproducts. Instead, they derive metabolic sustenance directly from the radiolytic decomposition...'"
                            ),
                            TestQuestion(
                                id = "qr1_3",
                                questionNumber = 3,
                                questionType = QuestionType.TRUE_FALSE_NOT_GIVEN,
                                prompt = "Deep basalt carbon capture has been adopted in more than fifty nations worldwide.",
                                options = listOf(
                                    QuestionOption("t", "TRUE", "Statement matches the passage"),
                                    QuestionOption("f", "FALSE", "Statement contradicts the passage"),
                                    QuestionOption("ng", "NOT GIVEN", "Information is not provided in passage")
                                ),
                                correctAnswer = "NOT GIVEN",
                                explanation = "The passage mentions pilot installations demonstrating 95% efficiency, but does not disclose the total number of nations adopting it."
                            )
                        )
                    )
                ),
                isCompleted = false,
                bestEstimatedBand = null
            )
        )
    )
    val mockTests: StateFlow<List<MockTest>> = _mockTests.asStateFlow()

    val demoWritingPrompts = listOf(
        WritingPrompt(
            id = "p_task1_demo",
            taskType = "Task 1 (Report)",
            title = "Global Renewable Energy Production (2010–2025)",
            instructions = "You should spend about 20 minutes on this task. Write at least 150 words. Summarize the information by selecting and reporting the main features, and make comparisons where relevant.",
            prompt = "The chart illustrates renewable electricity generation across Solar, Wind, and Hydroelectric sectors globally between 2010 and 2025 in Terawatt-hours (TWh). Summarize the key trends and comparative developments.",
            minWords = 150,
            suggestedTimeMinutes = 20,
            sampleHighBandEssay = "The provided graphic illustrates the developmental trajectory of clean electricity output..."
        ),
        WritingPrompt(
            id = "p_task2_live",
            taskType = "Task 2 (Essay)",
            title = "Artificial Intelligence in Higher Education",
            instructions = "You should spend about 40 minutes on this task. Write at least 250 words. Give reasons for your answer and include any relevant examples from your knowledge or experience.",
            prompt = "Some educators believe that artificial intelligence tools such as automated text generators will degrade students' genuine intellectual capabilities, while others argue that these technologies will elevate educational outcomes by personalizing learning. Discuss both views and give your own opinion.",
            minWords = 250,
            suggestedTimeMinutes = 40,
            sampleHighBandEssay = "In modern pedagogical debates, the role of generative computational models has sparked intense discourse..."
        )
    )

    val currentWritingPrompt: WritingPrompt
        get() = demoWritingPrompts[1]

    val demoSpeakingPrompts = listOf(
        SpeakingCueCard(
            id = "spk_p1_demo",
            part = 1,
            title = "Part 1: Hometown & Urban Modernization",
            prompt = "Let's talk about where you live. How has your city or neighborhood changed over the past five years? What facilities are most valued by local residents?",
            bulletPoints = listOf(
                "Describe your hometown location and geographical character",
                "Mention recent architectural and transit developments",
                "Explain whether you would recommend living there to international students"
            ),
            prepTimeSeconds = 15,
            speakTimeSeconds = 45,
            tips = "Speak naturally with varied lexical adjectives like 'vibrant metropolitan district' or 'bustling suburban hub'."
        ),
        SpeakingCueCard(
            id = "spk_cue_01",
            part = 2,
            title = "Part 2: Describe a Futuristic Technology You Would Like to Use",
            prompt = "You should say:\n• What this technology is\n• How you first learned about it\n• How it operates\n• And explain why you would like to use it in your daily life.",
            bulletPoints = listOf(
                "What the technology is (e.g. Brain-Computer Neural Interface or AI Holographic Studio)",
                "How and when you first learned about it",
                "What its fundamental mechanics or capabilities are",
                "Why you believe it would transform your professional or academic routine"
            ),
            prepTimeSeconds = 60,
            speakTimeSeconds = 120,
            tips = "Use Band 7.5+ discourse markers: 'Interestingly, I first stumbled upon...', 'From a technical standpoint...', 'What fascinates me most is...'"
        ),
        SpeakingCueCard(
            id = "spk_p3_demo",
            part = 3,
            title = "Part 3: Societal and Ethical Implications of Automation",
            prompt = "Do you believe automated intelligence will replace human mentors and teachers in the future? What measures should governments adopt to protect manual workforces?",
            bulletPoints = listOf(
                "Consider both economic efficiencies and empathetic human mentoring",
                "Evaluate regulatory frameworks and universal retraining initiatives",
                "Draw a balanced forward-looking conclusion for the next generation"
            ),
            prepTimeSeconds = 30,
            speakTimeSeconds = 90,
            tips = "Maintain formal academic discussion register; use speculative modal phrasing like 'One could reasonably argue that...'."
        )
    )

    val currentSpeakingCueCard: SpeakingCueCard
        get() = demoSpeakingPrompts[1]

    fun switchUserRole(newRole: UserRole) {
        _userProfile.update { it.copy(role = newRole) }
    }

    fun toggleMission(missionId: String) {
        _dailyMissions.update { list ->
            list.map {
                if (it.id == missionId) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }

    fun updateProfile(targetBand: Double, ieltsType: IeltsType, dailyMinutes: Int) {
        _userProfile.update {
            it.copy(
                targetBand = targetBand,
                ieltsType = ieltsType,
                dailyGoalMinutes = dailyMinutes
            )
        }
    }

    fun submitWritingEssay(essayText: String, wordCount: Int) {
        val newSub = WritingSubmission(
            id = "w_sub_${System.currentTimeMillis()}",
            studentName = _userProfile.value.name,
            promptId = currentWritingPrompt.id,
            taskType = currentWritingPrompt.taskType,
            promptTitle = currentWritingPrompt.title,
            essayText = essayText,
            wordCount = wordCount,
            submittedAt = "Just now",
            isEvaluated = false
        )
        _writingSubmissions.update { listOf(newSub) + it }
        // Update user study metrics
        _userProfile.update {
            it.copy(
                studyHoursTotal = it.studyHoursTotal + 0.7,
                xp = it.xp + 150
            )
        }
    }

    fun evaluateWritingSubmission(
        submissionId: String,
        tr: Double,
        cc: Double,
        lr: Double,
        gra: Double,
        feedback: String,
        strengths: String,
        weaknesses: String,
        corrections: String
    ) {
        val overall = BandScoreCalculator.calculateRubricBand(tr, cc, lr, gra)
        _writingSubmissions.update { list ->
            list.map { sub ->
                if (sub.id == submissionId) {
                    sub.copy(
                        isEvaluated = true,
                        taskResponseScore = tr,
                        coherenceCohesionScore = cc,
                        lexicalResourceScore = lr,
                        grammarScore = gra,
                        overallBand = overall,
                        teacherFeedback = feedback,
                        teacherStrengths = strengths,
                        teacherWeaknesses = weaknesses,
                        teacherCorrections = corrections
                    )
                } else sub
            }
        }
        // Update student writing band
        _userProfile.update { it.copy(writingBand = overall) }
    }

    fun submitSpeakingRecording(durationSeconds: Int) {
        val newSub = SpeakingSubmission(
            id = "spk_sub_${System.currentTimeMillis()}",
            studentName = _userProfile.value.name,
            cueCardTitle = currentSpeakingCueCard.title,
            part = currentSpeakingCueCard.part,
            durationSeconds = durationSeconds,
            recordedDate = "Just now",
            isEvaluated = false
        )
        _speakingSubmissions.update { listOf(newSub) + it }
        _userProfile.update {
            it.copy(
                studyHoursTotal = it.studyHoursTotal + 0.3,
                xp = it.xp + 100
            )
        }
    }

    fun toggleVocabMastery(vocabId: String) {
        _vocabList.update { list ->
            list.map { if (it.id == vocabId) it.copy(isMastered = !it.isMastered) else it }
        }
    }

    fun toggleVocabFavorite(vocabId: String) {
        _vocabList.update { list ->
            list.map { if (it.id == vocabId) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun addMockTest(newTest: MockTest) {
        _mockTests.update { list -> listOf(newTest) + list }
    }

    /**
     * AI-Assisted heuristic evaluation based on official IELTS 4-pillar rubrics:
     * - Task Response (word length, paragraph development)
     * - Coherence & Cohesion (discourse connectives, transitions)
     * - Lexical Resource (academic word list frequency, vocabulary variety)
     * - Grammatical Range & Accuracy (sentence complexity, clause subordination)
     */
    fun evaluateWritingAiAssisted(essayText: String, minWords: Int = 250): Map<String, Double> {
        val words = if (essayText.isBlank()) emptyList() else essayText.trim().split("\\s+".toRegex())
        val count = words.size

        val tr = when {
            count >= 280 -> 7.5
            count >= minWords -> 7.0
            count >= 200 -> 6.0
            count >= 150 -> 5.5
            else -> 4.5
        }

        val cohesiveMarkers = listOf("furthermore", "conversely", "consequently", "on the one hand", "on the other hand", "therefore", "in conclusion", "for instance", "moreover", "nonetheless", "whereas", "while")
        val lowerText = essayText.lowercase()
        val cohesiveCount = cohesiveMarkers.count { lowerText.contains(it) }
        val cc = when {
            cohesiveCount >= 5 -> 7.5
            cohesiveCount >= 3 -> 7.0
            cohesiveCount >= 1 -> 6.5
            else -> 5.5
        }

        val academicTokens = listOf("pedagogical", "proliferation", "substantiate", "mitigate", "exacerbate", "catalyst", "ubiquitous", "paradigm", "trajectories", "discourse", "empirical", "framework", "scaffolding", "inevitably")
        val academicCount = academicTokens.count { lowerText.contains(it) }
        val lr = when {
            academicCount >= 4 -> 8.0
            academicCount >= 2 -> 7.5
            academicCount >= 1 -> 7.0
            else -> 6.0
        }

        val complexMarkers = listOf("although", "which", "that", "provided", "if", "unless", "in order to", "while", "whereby")
        val complexCount = complexMarkers.count { lowerText.contains(it) }
        val gra = when {
            complexCount >= 5 -> 7.5
            complexCount >= 3 -> 7.0
            complexCount >= 1 -> 6.5
            else -> 6.0
        }

        val overall = BandScoreCalculator.calculateRubricBand(tr, cc, lr, gra)
        return mapOf(
            "TR" to tr,
            "CC" to cc,
            "LR" to lr,
            "GRA" to gra,
            "OVERALL" to overall
        )
    }

    companion object {
        @Volatile
        private var instance: IeltsRepository? = null

        fun getInstance(): IeltsRepository {
            return instance ?: synchronized(this) {
                instance ?: IeltsRepository().also { instance = it }
            }
        }
    }
}
