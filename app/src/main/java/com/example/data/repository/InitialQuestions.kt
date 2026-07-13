package com.example.data.repository

import com.example.data.model.Question

object InitialQuestions {
    val questions = listOf(
        // === MODULE 1: Early Life of Prophet ﷺ ===
        Question(
            text = "In which year was the Prophet Muhammad ﷺ born?",
            options = "The Year of the Elephant|The Year of Sorrow|The Year of delegations|The Year of Conquest",
            correctAnswer = "The Year of the Elephant",
            explanation = "The Prophet ﷺ was born in Makkah in the Year of the Elephant (circa 570 CE), corresponding to the year Abraha tried to attack the Kaaba.",
            category = "Birth",
            module = "Module 1"
        ),
        Question(
            text = "Who was the father of the Prophet Muhammad ﷺ?",
            options = "Abdullah|Abdul Muttalib|Abu Talib|Hamzah",
            correctAnswer = "Abdullah",
            explanation = "His father was Abdullah ibn Abd al-Muttalib, who passed away in Yathrib (Madinah) before the Prophet ﷺ was born.",
            category = "Family Tree",
            module = "Module 1"
        ),
        Question(
            text = "Who was the foster mother of the Prophet ﷺ who nursed him in the desert?",
            options = "Halimah al-Sa'diyyah|Aminah|Thuwaybah|Barakah (Umm Ayman)",
            correctAnswer = "Halimah al-Sa'diyyah",
            explanation = "Halimah al-Sa'diyyah of the Banu Sa'd tribe was his foster mother and raised him in the clean air of the desert during his infancy.",
            category = "Foster Mother Halimah (RA)",
            module = "Module 1"
        ),
        Question(
            text = "At what age did the Prophet's ﷺ mother, Aminah, pass away?",
            options = "6 years old|8 years old|10 years old|12 years old",
            correctAnswer = "6 years old",
            explanation = "Aminah passed away at Al-Abwa (between Makkah and Madinah) when the Prophet ﷺ was six years old, leaving him under the guardianship of his grandfather, Abdul Muttalib.",
            category = "Childhood",
            module = "Module 1"
        ),
        Question(
            text = "How old was the Prophet ﷺ when he married Khadijah (RA)?",
            options = "25 years old|30 years old|35 years old|40 years old",
            correctAnswer = "25 years old",
            explanation = "The Prophet ﷺ married Khadijah bint Khuwaylid (RA) when he was 25 years old, and she was 40 years old, after she became impressed with his honesty (Al-Amin) in trade.",
            category = "Marriage with Khadijah (RA)",
            module = "Module 1"
        ),

        // === MODULE 2: Beginning of Prophethood ===
        Question(
            text = "Where did the Prophet Muhammad ﷺ receive the first revelation from Angel Jibril (AS)?",
            options = "Cave Hira|Cave Thawr|Mount Safa|Masjid al-Haram",
            correctAnswer = "Cave Hira",
            explanation = "The first revelation descended upon the Prophet ﷺ in Cave Hira on Mount al-Noor (the Mountain of Light) during the month of Ramadan.",
            category = "Cave Hira",
            module = "Module 2"
        ),
        Question(
            text = "Which verses of the Quran were revealed first?",
            options = "Surah Al-Alaq (Verses 1-5)|Surah Al-Muzzammil (Verses 1-5)|Surah Al-Fatiha|Surah Al-Muddaththir (Verses 1-5)",
            correctAnswer = "Surah Al-Alaq (Verses 1-5)",
            explanation = "The first five verses of Surah Al-Alaq ('Read! In the name of your Lord who created...') were the very first verses revealed to the Prophet ﷺ.",
            category = "First Revelation",
            module = "Module 2"
        ),
        Question(
            text = "Who was the first adult male to accept Islam?",
            options = "Abu Bakr Al-Siddiq (RA)|Ali ibn Abi Talib (RA)|Zayd ibn Harithah (RA)|Uthman ibn Affan (RA)",
            correctAnswer = "Abu Bakr Al-Siddiq (RA)",
            explanation = "Abu Bakr Al-Siddiq (RA) was the first adult male outside the Prophet's ﷺ household to accept Islam, immediately believing him without hesitation.",
            category = "Early Muslims",
            module = "Module 2"
        ),
        Question(
            text = "For how many years did the Prophet ﷺ conduct Secret Dawah before public propagation?",
            options = "3 years|1 year|5 years|7 years",
            correctAnswer = "3 years",
            explanation = "The Prophet ﷺ called people to Islam secretly for three years to build a strong foundation of early Muslims before receiving command to preach publicly.",
            category = "Secret Dawah",
            module = "Module 2"
        ),
        Question(
            text = "Why is the 10th year of Prophethood called the 'Year of Sorrow' ('Aam al-Huzn)?",
            options = "Death of Abu Talib and Khadijah (RA)|The Boycott of Banu Hashim|The rejection of Taif|The migration to Abyssinia",
            correctAnswer = "Death of Abu Talib and Khadijah (RA)",
            explanation = "The Year of Sorrow represents the loss of Abu Talib (his protective uncle) and Khadijah (his loving wife and supporter) within a short span of time.",
            category = "Year of Sorrow",
            module = "Module 2"
        ),

        // === MODULE 3: Hijrah and Madinah ===
        Question(
            text = "In which cave did the Prophet ﷺ and Abu Bakr (RA) hide during their migration to Madinah?",
            options = "Cave Thawr|Cave Hira|Cave of Seven Sleepers|Cave Uhud",
            correctAnswer = "Cave Thawr",
            explanation = "They hid in Cave Thawr for three days to evade the Makkah search parties before continuing their journey to Yathrib (Madinah).",
            category = "Migration to Madinah",
            module = "Module 3"
        ),
        Question(
            text = "What was the very first Mosque built by the Prophet ﷺ and early Muslims?",
            options = "Masjid Quba|Masjid Nabawi|Masjid al-Qiblatayn|Masjid al-Haram",
            correctAnswer = "Masjid Quba",
            explanation = "Masjid Quba, located on the outskirts of Madinah, was the first mosque built by the Prophet ﷺ upon arriving in the area during the Hijrah.",
            category = "Masjid Nabawi",
            module = "Module 3"
        ),
        Question(
            text = "What was the name of the formal pact of brotherhood established between the Muhajirun and Ansar?",
            options = "Muwakhat|Hilf al-Fudul|Treaty of Hudaybiyyah|Constitution of Madinah",
            correctAnswer = "Muwakhat",
            explanation = "Muwakhat (Fraternization) was the bond established by the Prophet ﷺ, pairing each emigrant (Muhajir) with a local helper (Ansari) to share resources.",
            category = "Brotherhood",
            module = "Module 3"
        ),
        Question(
            text = "What was the Constitution of Madinah primarily designed to do?",
            options = "Establish peace and alliance between all Madinan tribes including Jewish tribes|Declare war on Makkah|Appoint leaders for battles|Specify pilgrimage rules",
            correctAnswer = "Establish peace and alliance between all Madinan tribes including Jewish tribes",
            explanation = "The Constitution of Madinah was a historic document outlining the rights, duties, and peaceful co-existence of all Muslims, Jews, and pagan tribes.",
            category = "Constitution of Madinah",
            module = "Module 3"
        ),

        // === MODULE 4: Madinan Period ===
        Question(
            text = "In which year AH (After Hijrah) was the Qibla (direction of prayer) changed from Jerusalem to Makkah?",
            options = "2 AH|1 AH|3 AH|5 AH",
            correctAnswer = "2 AH",
            explanation = "The changing of the Qibla occurred in Shaban of 2 AH, during prayer, when Allah revealed Surah Al-Baqarah verse 144.",
            category = "Social Reforms",
            module = "Module 4"
        ),
        Question(
            text = "Which of the Jewish tribes of Madinah was the first to violate their treaty after Badr?",
            options = "Banu Qaynuqa|Banu Nadir|Banu Qurayzah|Banu Mustaliq",
            correctAnswer = "Banu Qaynuqa",
            explanation = "Banu Qaynuqa was the first tribe exiled from Madinah after violating their treaty obligations and initiating conflict with Muslims.",
            category = "Jewish Tribes",
            module = "Module 4"
        ),

        // === MODULE 5: Last Years & Farewell Sermon ===
        Question(
            text = "In which Islamic year did the bloodless Conquest of Makkah occur?",
            options = "8 AH|6 AH|10 AH|9 AH",
            correctAnswer = "8 AH",
            explanation = "The Conquest of Makkah took place in Ramadan of 8 AH, resulting in a peaceful victory and general amnesty for all former adversaries.",
            category = "Conquest of Makkah",
            module = "Module 5"
        ),
        Question(
            text = "Where did the Prophet ﷺ deliver the famous Farewell Sermon (Khutbah al-Wada)?",
            options = "Mount Arafat (Uranah Valley)|Mount Safa|Masjid Nabawi|Mina",
            correctAnswer = "Mount Arafat (Uranah Valley)",
            explanation = "The Farewell Sermon was delivered on Mount Arafat (Mount of Mercy) during the Farewell Pilgrimage in 10 AH, attended by over 100,000 Muslims.",
            category = "Farewell Hajj",
            module = "Module 5"
        ),
        Question(
            text = "Which key moral principle was heavily emphasized in the Farewell Sermon?",
            options = "All mankind is equal, no race is superior to another|Mandatory trade journeys|Immediate expansion of territory|Rules of building high castles",
            correctAnswer = "All mankind is equal, no race is superior to another",
            explanation = "The Prophet ﷺ declared: 'An Arab has no superiority over a non-Arab, nor does a non-Arab have any superiority over an Arab... except by piety and good action.'",
            category = "Last Sermon",
            module = "Module 5"
        ),
        Question(
            text = "On which day and year did the Prophet Muhammad ﷺ return to his Lord?",
            options = "12th Rabi al-Awwal, 11 AH|10th Ramadan, 10 AH|12th Rabi al-Awwal, 10 AH|1st Muharram, 12 AH",
            correctAnswer = "12th Rabi al-Awwal, 11 AH",
            explanation = "The Prophet ﷺ passed away on Monday, the 12th of Rabi' al-Awwal in the 11th year of Hijrah (632 CE) in his room in Madinah.",
            category = "Final Days",
            module = "Module 5"
        ),

        // === BATTLE MODULES ===
        Question(
            text = "How many Muslims fought alongside the Prophet ﷺ in the Battle of Badr?",
            options = "313|1000|700|10,000",
            correctAnswer = "313",
            explanation = "In the Battle of Badr (2 AH), the Muslims numbered 313, poorly equipped, but victorious against a fully armed Quraysh army of 1,000.",
            category = "Badr",
            module = "Battle"
        ),
        Question(
            text = "What was the main reason for the setback in the Battle of Uhud?",
            options = "The archers left their positions on the mount prematurely|The desert storm|The betrayal of the cavalry|Numerical superiority of Quraysh",
            correctAnswer = "The archers left their positions on the mount prematurely",
            explanation = "Despite initial success, the group of archers stationed on the mount left their positions thinking the battle was won, allowing Khalid ibn Walid (then pagan) to flank them.",
            category = "Uhud",
            module = "Battle"
        ),
        Question(
            text = "Which Companion suggested digging a trench (Khandaq) to protect Madinah?",
            options = "Salman al-Farsi (RA)|Bilal ibn Rabah (RA)|Abu Bakr (RA)|Ali ibn Abi Talib (RA)",
            correctAnswer = "Salman al-Farsi (RA)",
            explanation = "Salman al-Farsi (RA), who was familiar with Persian defensive strategies, suggested digging a wide trench around vulnerable borders of Madinah.",
            category = "Khandaq (Ahzab)",
            module = "Battle"
        ),
        Question(
            text = "In which year AH did the conquest of the highly fortified oasis of Khaybar occur?",
            options = "7 AH|5 AH|8 AH|9 AH",
            correctAnswer = "7 AH",
            explanation = "The Expedition and Battle of Khaybar occurred in 7 AH to neutralize the threat of hostile coalitions planning attacks against Madinah.",
            category = "Khaybar",
            module = "Battle"
        ),
        Question(
            text = "Who was appointed commander of the Muslim army at Mu'tah after the martyrdom of Zayd, Ja'far, and Abdullah?",
            options = "Khalid ibn al-Walid (RA)|Usamah ibn Zayd (RA)|Sa'd ibn Abi Waqqas (RA)|Abu Ubaydah (RA)",
            correctAnswer = "Khalid ibn al-Walid (RA)",
            explanation = "Khalid ibn al-Walid (RA) assumed command, successfully executed a brilliant tactical retreat against overwhelming Byzantine forces, and was dubbed 'The Sword of Allah' (Sayfullah).",
            category = "Mu'tah",
            module = "Battle"
        ),
        Question(
            text = "The Battle of Tabuk (9 AH) was launched in response to an impending invasion by which empire?",
            options = "The Roman (Byzantine) Empire|The Persian (Sasanian) Empire|The Abyssinian Empire|The Ghassanid Kingdom",
            correctAnswer = "The Roman (Byzantine) Empire",
            explanation = "The Prophet ﷺ led an army of 30,000 to Tabuk to counter a rumored Byzantine (Roman) buildup, demonstrating Muslim strength; no battle occurred.",
            category = "Tabuk",
            module = "Battle"
        ),
        Question(
            text = "Which battle took place shortly after the Conquest of Makkah in a steep valley?",
            options = "Battle of Hunayn|Battle of Tabuk|Battle of Khaybar|Battle of Mu'tah",
            correctAnswer = "Battle of Hunayn",
            explanation = "The Battle of Hunayn occurred in 8 AH in a steep valley against the Hawazin and Thaqif tribes who gathered forces to attack the Muslims.",
            category = "Hunayn",
            module = "Battle"
        )
    )
}
