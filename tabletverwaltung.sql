-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 30. Nov 2016 um 23:17
-- Server-Version: 10.1.8-MariaDB
-- PHP-Version: 5.6.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `tabletverwaltung`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hardware`
--

CREATE TABLE `hardware` (
  `har_id` int(11) NOT NULL,
  `har_seriennummer` varchar(50) NOT NULL,
  `har_beschreibung` varchar(128) DEFAULT NULL,
  `har_bemerkung` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONEN DER TABELLE `hardware`:
--

--
-- Daten für Tabelle `hardware`
--

INSERT INTO `hardware` (`har_id`, `har_seriennummer`, `har_beschreibung`, `har_bemerkung`) VALUES
(1701, '010949553352', 'Surface 10', ''),
(1703, '031392352652', 'Surface 10', ''),
(1704, '010435160852', 'Surface 10', ''),
(1705, '010518660852', 'Surface 10', ''),
(1706, '010239653252', 'Surface 10', ''),
(1707, '009268354852', 'Surface 10', ''),
(1708, '010727460852', 'Surface 10', '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `historie`
--

CREATE TABLE `historie` (
  `his_id` int(11) NOT NULL,
  `his_verliehen_durch` int(11) NOT NULL,
  `his_verliehen_an` int(11) NOT NULL,
  `his_datum_verleih` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `his_datum_rueckgabe` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `his_kurs` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONEN DER TABELLE `historie`:
--   `his_kurs`
--       `kurs` -> `kur_id`
--   `his_verliehen_an`
--       `schueler` -> `sch_id`
--   `his_verliehen_durch`
--       `lehrer` -> `leh_id`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `kurs`
--

CREATE TABLE `kurs` (
  `kur_id` int(11) NOT NULL,
  `kur_name` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONEN DER TABELLE `kurs`:
--

--
-- Daten für Tabelle `kurs`
--

INSERT INTO `kurs` (`kur_id`, `kur_name`) VALUES
(1, 'EKMBME'),
(2, 'JG1MBME'),
(3, 'JG2MBME'),
(4, 'EKMEET'),
(5, 'JG1MEET'),
(6, 'JG2MEET'),
(7, 'EKITTI'),
(8, 'JG1ITTI'),
(9, 'JG2ITTI'),
(10, 'EKITSE'),
(11, 'JG1ITSE'),
(12, 'JG2ITSE'),
(13, 'JG1ITSG'),
(14, 'JG2ITSG'),
(15, 'EKAITPRO'),
(16, 'EKUTET'),
(17, 'JG1UTET'),
(18, 'JG2UTET'),
(19, 'EKUTVT'),
(20, 'JG1UTVT'),
(21, 'JG2UTVT'),
(22, 'EKCTME_1'),
(23, 'EKCTME_2'),
(24, 'EKCTIT_1'),
(25, 'EKCTIT_2'),
(28, 'EKCTUT_1'),
(29, 'EKCTUT_2'),
(30, 'JGCT01'),
(31, 'JGCT02_1'),
(32, 'JGCT02_2'),
(33, 'JGCT03'),
(34, 'JGCT04'),
(35, 'JGCT05'),
(36, 'EKMME'),
(37, 'EKMIT'),
(38, 'JG1MME'),
(39, 'JG1MIT'),
(40, 'JG1MUT'),
(41, 'JG2MIT'),
(42, 'JG2MUT'),
(43, 'EKDME'),
(44, 'EKDUT'),
(45, 'JG1DME'),
(46, 'JG1DIT'),
(47, 'JG1DUT'),
(48, 'JG2DIT'),
(49, 'JG2DUT'),
(50, 'EKE1'),
(51, 'EKE2'),
(52, 'JG1EME'),
(53, 'JG1EIT'),
(54, 'JG1EUT'),
(55, 'JG2EIT'),
(56, 'JG2EUT'),
(57, 'EKGGKME'),
(58, 'EKGGKUT'),
(59, 'JG1GGKME'),
(60, 'JG1GGKIT'),
(61, 'JG2GGKIT'),
(62, 'JG2GGKUT'),
(63, 'EKPHYME'),
(64, 'EKPHY-LME'),
(65, 'EKPHY-LIT'),
(66, 'EKPHYUT'),
(67, 'EKPHY-LUT'),
(68, 'EKCHEMME'),
(69, 'EKCHEMUT'),
(70, 'JG1PHYIT'),
(73, 'JG1PHYME'),
(74, 'JG2PHYME'),
(75, 'JG1CHEMME'),
(76, 'JG2CHEMME'),
(77, 'EKRELKATH'),
(78, 'JG1RELKATH'),
(79, 'JG2RELKATH'),
(80, 'EKRELEV'),
(81, 'JG1RELEV'),
(82, 'JG2RELEV'),
(83, 'EKETHIK'),
(84, 'JG1ETHIK'),
(85, 'JG2ETHIK'),
(87, 'EKSPORTME'),
(88, 'EKSPORTUT'),
(89, 'JG1SPORTME'),
(90, 'JG1SPORTUT'),
(91, 'JG2SPORTME'),
(92, 'JG2SPORTUT'),
(93, 'EKBNK'),
(94, 'JGBNK'),
(96, 'EKME/ETME'),
(97, 'JG1ETME'),
(98, 'JG1VGUT'),
(99, 'EKGSME'),
(100, 'JG1GSME'),
(101, 'EKFBME'),
(102, 'JG1FBME'),
(103, 'JG2FBME'),
(104, 'EKSBME'),
(105, 'JG1SBME'),
(106, 'JG2SBME'),
(107, 'EKMETHODEME'),
(108, 'EKMETHODEIT'),
(109, 'JG1SEMINARKURS'),
(110, 'JG1LIT+THEATER'),
(111, 'JG2LIT+THEATER'),
(112, 'JG1WI1'),
(113, 'JG1WI2'),
(114, 'JG2WK_1'),
(115, 'JG2WK_2'),
(117, ' JG1CHEM');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `lehrer`
--

CREATE TABLE `lehrer` (
  `leh_id` int(11) NOT NULL,
  `leh_name` varchar(64) DEFAULT NULL,
  `leh_vorname` varchar(64) DEFAULT NULL,
  `leh_kuerzel` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONEN DER TABELLE `lehrer`:
--

--
-- Daten für Tabelle `lehrer`
--

INSERT INTO `lehrer` (`leh_id`, `leh_name`, `leh_vorname`, `leh_kuerzel`) VALUES
(1, 'Heeger', 'Klaus', 'hg'),
(2, 'Edinger', 'Jürgen', 'ed'),
(3, 'Schmidt-Staub', 'Andreas', 'sm'),
(4, 'Schlütter', 'Bernd', 'sue'),
(5, 'Geider-Klary', 'Manfred', 'gk'),
(6, 'Baier', 'Klaus', 'bi'),
(8, 'Weindel', 'Klaus', 'wd'),
(10, 'Kleinbongardt', 'Frank', 'kb'),
(11, 'Hägele', 'Jan', 'hae'),
(12, 'Kopizenski', 'Ulrike', 'ki'),
(13, 'Grötzinger', 'Friedemann', 'gz'),
(15, 'Rensch', 'Eckart', 'rs'),
(18, 'Muller', 'Claude', 'mu'),
(19, 'Seiler', 'Dominik', 'se'),
(21, 'Becker', 'Jürgen', 'be'),
(24, 'Karrer', 'Thomas', 'ka'),
(25, 'Lapos', 'Martin', 'la'),
(26, 'Wellenreuther', 'Katrin', 'wr'),
(27, 'Geis', 'Klaus', 'ge'),
(28, 'Bucher', 'Bodo', 'bc'),
(29, 'Steeb', 'Thomas', 'sb'),
(30, 'Lauer', 'Ulrich', 'lu'),
(31, 'Hilber', 'Barbara', 'hi'),
(32, 'Michaelis', 'Dorle', 'mi'),
(33, 'Diettrich', 'Rudolf', 'dt'),
(35, 'Dreier', 'Claudia', 'dr'),
(36, 'Güngör', 'Dursun', 'gü'),
(38, 'Reinicke', 'Petra', 'rn'),
(40, 'Rausch', 'Hartwig', 'rh'),
(41, 'Göhlich', 'Tanja', 'goe'),
(44, 'Gleich', 'Evelyn', 'gl'),
(46, 'Dörflinger', 'Jürgen', 'df'),
(47, 'Fritz', 'Simone', 'fz'),
(49, 'Eiseler', 'Daniel', 'el'),
(50, 'Haas', 'Roman', 'hs'),
(51, 'John', 'Georg', 'jo'),
(54, 'Drexel', 'Anna', 'dx'),
(55, 'Starke', 'unbekannt', 'sta'),
(57, 'Kübler', 'Raimund', 'kr'),
(58, 'Laudan', 'Sophie', 'ld'),
(59, 'Admin', 'Elks', 'elksadmin'),
(60, 'Fuchs', 'Diane', 'fu'),
(61, 'Egolf', 'Carsten', 'ef'),
(62, 'Busch', 'Hanno', 'bu'),
(64, 'Kuhl', 'Wolfgang', 'ku'),
(65, 'Gräser', 'Ulrich', 'gr'),
(67, 'Freund-Holschuh', 'Christine', 'fh'),
(68, 'Reisch', 'Kristiane', 'rk'),
(69, 'Utz', 'Jürgen', 'ut'),
(70, 'Garben', 'Katharina', 'ga'),
(71, 'Dansauer', 'Dansauer', 'da'),
(72, 'Sieber', 'Bettina', 'si'),
(73, 'Kornmeyer', 'Richard', 'ko'),
(78, 'Scheffzyk', 'Sabine', 'sf'),
(79, 'Denzler', 'Simone', 'den'),
(80, 'Matejka', 'Volker', 'ma'),
(81, 'Abramova', 'Alexandra', 'ab'),
(82, 'Rettig', 'Sandra', 're'),
(83, 'Benz', 'Martin', 'bz');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `schueler`
--

CREATE TABLE `schueler` (
  `sch_id` int(11) NOT NULL,
  `sch_name` varchar(50) NOT NULL,
  `sch_vorname` varchar(50) NOT NULL,
  `sch_kla_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- RELATIONEN DER TABELLE `schueler`:
--   `sch_kla_id`
--       `kurs` -> `kur_id`
--

--
-- Daten für Tabelle `schueler`
--

INSERT INTO `schueler` (`sch_id`, `sch_name`, `sch_vorname`, `sch_kla_id`) VALUES
(1, 'Brinkmann', 'Lukas', 1),
(2, 'Lautenschläger', 'Alexander', 1),
(3, 'Mustermann', 'Max', 5),
(4, 'Schäfer', 'Dennis', 5);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `hardware`
--
ALTER TABLE `hardware`
  ADD PRIMARY KEY (`har_id`);

--
-- Indizes für die Tabelle `historie`
--
ALTER TABLE `historie`
  ADD PRIMARY KEY (`his_id`);

--
-- Indizes für die Tabelle `kurs`
--
ALTER TABLE `kurs`
  ADD PRIMARY KEY (`kur_id`);

--
-- Indizes für die Tabelle `lehrer`
--
ALTER TABLE `lehrer`
  ADD PRIMARY KEY (`leh_id`);

--
-- Indizes für die Tabelle `schueler`
--
ALTER TABLE `schueler`
  ADD PRIMARY KEY (`sch_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `kurs`
--
ALTER TABLE `kurs`
  MODIFY `kur_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=118;
--
-- AUTO_INCREMENT für Tabelle `schueler`
--
ALTER TABLE `schueler`
  MODIFY `sch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
