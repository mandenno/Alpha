-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 26, 2019 at 03:23 PM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tbcappor_alpha`
--

-- --------------------------------------------------------

--
-- Table structure for table `bdo_org`
--

CREATE TABLE `bdo_org` (
  `organization_id` int(200) NOT NULL,
  `organization_name` varchar(200) NOT NULL,
  `org_code` varchar(10) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `location` varchar(200) NOT NULL,
  `password` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bdo_org`
--

INSERT INTO `bdo_org` (`organization_id`, `organization_name`, `org_code`, `phone`, `location`, `password`) VALUES
(1, 'BDO ORG', '2222', '+254708692173', 'Mombasa', 1234),
(2, 'BDO', '1234', '+254791936784', 'MOMBASA', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `blood_bank`
--

CREATE TABLE `blood_bank` (
  `id` int(200) NOT NULL,
  `blood_group` varchar(200) NOT NULL,
  `quantity` int(200) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `blood_recieved`
--

CREATE TABLE `blood_recieved` (
  `id` int(11) NOT NULL,
  `blood_type` varchar(250) NOT NULL,
  `blood_rhesus` varchar(250) NOT NULL,
  `Qr_id` varchar(250) NOT NULL,
  `Quantity` int(230) NOT NULL,
  `Date` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `blood_recieved`
--

INSERT INTO `blood_recieved` (`id`, `blood_type`, `blood_rhesus`, `Qr_id`, `Quantity`, `Date`) VALUES
(1, 'B+', '', '55445', 56, '2019-07-03'),
(2, 'A-', '', 'qr234', 4, '2019-07-03'),
(3, 'B-', '', 'qr235', 6, '2019-07-03'),
(4, 'AB+', '', 'qr236', 3, '2019-07-03'),
(5, 'B+', '', 'qr237', 6, '2019-07-03'),
(6, 'O+', '', 'qr238', 8, '2019-07-03'),
(7, 'O-', '', 'qr239', 3, '2019-07-03');

-- --------------------------------------------------------

--
-- Table structure for table `blood_registration`
--

CREATE TABLE `blood_registration` (
  `id` int(200) NOT NULL,
  `bdo_id` varchar(40) NOT NULL,
  `qr_bag_no` varchar(200) NOT NULL,
  `donor_id` int(200) NOT NULL,
  `campaign` text NOT NULL,
  `description` text NOT NULL,
  `location` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `blood_registration`
--

INSERT INTO `blood_registration` (`id`, `bdo_id`, `qr_bag_no`, `donor_id`, `campaign`, `description`, `location`) VALUES
(9, '1234', '1234', 1234, 'Blood for Maternal Mothers', 'Raising Blood for Maternal Mothers', 'Mombasa'),
(10, '', '', 0, 'Blood for Maternal Mothers', 'Raising Blood for Maternal Mothers', 'Mombasa');

-- --------------------------------------------------------

--
-- Table structure for table `Blood_requests`
--

CREATE TABLE `Blood_requests` (
  `id` int(11) NOT NULL,
  `blood_type` varchar(250) NOT NULL,
  `blood_rhesus` varchar(250) NOT NULL,
  `quatity` varchar(250) NOT NULL,
  `hospital_id` varchar(250) NOT NULL,
  `date` datetime(6) NOT NULL,
  `date_required` datetime(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Blood_requests`
--

INSERT INTO `Blood_requests` (`id`, `blood_type`, `blood_rhesus`, `quatity`, `hospital_id`, `date`, `date_required`) VALUES
(1, 'A', 'A-', '4', 'Mom034', '2019-07-03 11:11:15.437039', '2019-07-05 16:24:12.390203'),
(2, 'O', 'O-', '2', 'Mom034', '2019-07-03 11:20:22.242273', '2019-07-06 23:00:00.000000'),
(3, 'AB', 'AB-', '1', 'Mom034', '2019-07-03 11:30:39.780125', '2019-07-05 11:10:15.148000'),
(4, 'B', 'B+', '5', 'Mom034', '2019-07-05 11:36:36.546320', '2019-07-05 09:11:18.000000');

-- --------------------------------------------------------

--
-- Table structure for table `blood_screening`
--

CREATE TABLE `blood_screening` (
  `id` int(200) NOT NULL,
  `donor_id` int(200) NOT NULL,
  `blood_group` varchar(200) NOT NULL,
  `notes` varchar(200) NOT NULL,
  `status` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `blood_screening`
--

INSERT INTO `blood_screening` (`id`, `donor_id`, `blood_group`, `notes`, `status`) VALUES
(12, 1234, '1234', 'tyy', 'Approved');

-- --------------------------------------------------------

--
-- Table structure for table `blood_stock`
--

CREATE TABLE `blood_stock` (
  `id` int(200) NOT NULL,
  `donor_id` int(200) NOT NULL,
  `blood_type` varchar(200) NOT NULL,
  `registered_date` date NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `blood_stock`
--

INSERT INTO `blood_stock` (`id`, `donor_id`, `blood_type`, `registered_date`) VALUES
(1, 1234, 'B+', '2019-07-03');

-- --------------------------------------------------------

--
-- Table structure for table `campaigns`
--

CREATE TABLE `campaigns` (
  `id` int(11) NOT NULL,
  `body` varchar(200) NOT NULL,
  `campaign_title` text NOT NULL,
  `campaign_description` text NOT NULL,
  `drive_date` varchar(100) NOT NULL,
  `drive_time` varchar(50) NOT NULL,
  `date_reg` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `campaigns`
--

INSERT INTO `campaigns` (`id`, `body`, `campaign_title`, `campaign_description`, `drive_date`, `drive_time`, `date_reg`) VALUES
(1, 'Red Cross', 'Red Cross blood drive Mombasa', 'Red Cross will be undertaking the blood drive in Mombasa at HC.', '23/09/2019', '8:00 AM- 4:00PM', '2019-07-03 08:26:05');

-- --------------------------------------------------------

--
-- Table structure for table `donated`
--

CREATE TABLE `donated` (
  `id` int(11) NOT NULL,
  `qr_code` varchar(40) NOT NULL,
  `patient_id` varchar(100) NOT NULL,
  `datereg` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `donor_registration`
--

CREATE TABLE `donor_registration` (
  `id` int(200) NOT NULL,
  `donor_id` varchar(20) NOT NULL,
  `donor_name` varchar(200) NOT NULL,
  `age` int(200) NOT NULL,
  `weight` int(200) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `password` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `donor_registration`
--

INSERT INTO `donor_registration` (`id`, `donor_id`, `donor_name`, `age`, `weight`, `phone`, `address`, `password`) VALUES
(1, '1234', 'Dennis Onkangi', 25, 67, '0721107552', 'denonkangi@gmail.com', '1234'),
(2, '1222', 'Dennis', 25, 58, '588', '', '8537'),
(3, '1223', 'joshua', 25, 58, '0799659252', '', '8863'),
(4, '325556', 'Jetemiah', 58, 90, '0723562087', '', '1022'),
(5, '12345', 'peter', 25, 55, '0759166281', '', '3179');

-- --------------------------------------------------------

--
-- Table structure for table `hospital`
--

CREATE TABLE `hospital` (
  `hospital_id` int(200) NOT NULL,
  `hospital_name` varchar(200) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `level` varchar(200) NOT NULL,
  `status` varchar(200) NOT NULL,
  `county` varchar(200) NOT NULL,
  `contact_info` varchar(200) NOT NULL,
  `password` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hospital`
--

INSERT INTO `hospital` (`hospital_id`, `hospital_name`, `phone`, `level`, `status`, `county`, `contact_info`, `password`) VALUES
(1, 'MBO HEALTH', '+254708692173', 'Level 4', 'private', 'Mombasa', '+254708692173', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `hospital_donated`
--

CREATE TABLE `hospital_donated` (
  `id` int(11) NOT NULL,
  `qr_code` varchar(100) NOT NULL,
  `patient_id` varchar(50) NOT NULL,
  `datereg` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hospital_donated`
--

INSERT INTO `hospital_donated` (`id`, `qr_code`, `patient_id`, `datereg`) VALUES
(1, '2556', 'Patient ID: 2354', '2019-07-05 06:08:56'),
(2, '', '', '2019-07-05 09:47:57');

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
  `id` int(11) NOT NULL,
  `donor_id` int(11) NOT NULL,
  `campaign` varchar(100) NOT NULL,
  `subject` text NOT NULL,
  `message` text NOT NULL,
  `datereg` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `logs`
--

INSERT INTO `logs` (`id`, `donor_id`, `campaign`, `subject`, `message`, `datereg`) VALUES
(1, 1234, 'Red Cross', 'Donation Received', 'Thank you for donating blood to Red Cross Kenya. Your blood has been take for screening.', '2019-07-03 06:25:45'),
(10, 1234, 'Blood regional center', 'Blood Screened!', ' Congratulations! , Your blood has been screened successfully. We have taken for storage and donate to the need patient.', '2019-07-04 16:58:58'),
(24, 0, 'Blood for Maternal Mothers', 'Donation Received!', ' Hello , Thank you for donating blood to ALPHA Kenya. Your blood has been taken for screening', '2019-07-06 08:59:50'),
(23, 1234, 'Alpha Medical Center', 'Blood given to patient', 'Congratulations Dennis Onkangi!, Your blood has been donated to a need patient. Thank you for saving a life!', '2019-07-05 09:47:57'),
(22, 1234, 'Blood regional center', 'Blood Screened!', 'Congratulations! Dennis Onkangi, Your blood has been screened successfully your blood group is (AB+). We have taken it for storage and donate to the need patient.', '2019-07-05 09:47:05'),
(21, 1234, 'Blood regional center', 'Blood Screened!', 'Congratulations! Dennis Onkangi, Your blood has been screened successfully your blood group is (AB+). We have taken it for storage and donate to the need patient.', '2019-07-05 07:53:35'),
(20, 1234, 'Blood for Maternal Mothers', 'Donation Received!', ' Hello Mohammed Riyaz Virji, Thank you for donating blood to ALPHA Kenya. Your blood has been taken for screening', '2019-07-05 07:30:24'),
(18, 1234, 'Blood for Maternal Mothers', 'Donation Received!', ' Hello Mohammed Riyaz Virji, Thank you for donating blood to ALPHA Kenya. Your blood has been taken for screening', '2019-07-04 18:17:00'),
(19, 1234, 'Alpha Medical Center', 'Blood given to patient', 'Congratulations Mohammed Riyaz Virji!, Your blood has been donated to a need patient. Thank you for saving a life!', '2019-07-04 22:33:41');

-- --------------------------------------------------------

--
-- Table structure for table `regional_centre`
--

CREATE TABLE `regional_centre` (
  `id` int(200) NOT NULL,
  `regional_id` varchar(200) NOT NULL,
  `regional_name` varchar(200) NOT NULL,
  `regional_location` varchar(200) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `password` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `regional_centre`
--

INSERT INTO `regional_centre` (`id`, `regional_id`, `regional_name`, `regional_location`, `phone`, `password`) VALUES
(1, '1234', 'Regional Blood Transfusion Centre', 'Mombasa', '+254791936784', 1234),
(2, '1235', 'BHD ', 'Mombasa', '+254708692173', 1234),
(3, '1234', 'MOMBASA REGIONAL BLOOD CENTER', 'MOMBASA', '+254723250731', 1234);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `userole` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `emailid` varchar(255) NOT NULL,
  `lastlogin` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `userole`, `password`, `name`, `emailid`, `lastlogin`) VALUES
(1, 'admin', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'Lewa', 'joshua@murithi.com', '0000-00-00 00:00:00'),
(0, 'admin', 'hospital', '21232f297a57a5a743894a0e4a801fc3', 'Lewa', 'joshua@murithi.com', '0000-00-00 00:00:00'),
(0, 'bdo', 'BDO', '21232f297a57a5a743894a0e4a801fc3', 'Lewa', 'BDO@gmail.com', '0000-00-00 00:00:00'),
(0, 'regional', 'regional', '21232f297a57a5a743894a0e4a801fc3', 'Lewa', 'BDO@gmail.com', '0000-00-00 00:00:00'),
(1, 'admin', 'admin', '1234', 'Lewa', 'joshua@murithi.com', '0000-00-00 00:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bdo_org`
--
ALTER TABLE `bdo_org`
  ADD PRIMARY KEY (`organization_id`);

--
-- Indexes for table `blood_bank`
--
ALTER TABLE `blood_bank`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blood_recieved`
--
ALTER TABLE `blood_recieved`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blood_registration`
--
ALTER TABLE `blood_registration`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Blood_requests`
--
ALTER TABLE `Blood_requests`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blood_screening`
--
ALTER TABLE `blood_screening`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blood_stock`
--
ALTER TABLE `blood_stock`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `campaigns`
--
ALTER TABLE `campaigns`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `donated`
--
ALTER TABLE `donated`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `donor_registration`
--
ALTER TABLE `donor_registration`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `hospital`
--
ALTER TABLE `hospital`
  ADD PRIMARY KEY (`hospital_id`);

--
-- Indexes for table `hospital_donated`
--
ALTER TABLE `hospital_donated`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `regional_centre`
--
ALTER TABLE `regional_centre`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bdo_org`
--
ALTER TABLE `bdo_org`
  MODIFY `organization_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `blood_bank`
--
ALTER TABLE `blood_bank`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `blood_recieved`
--
ALTER TABLE `blood_recieved`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `blood_registration`
--
ALTER TABLE `blood_registration`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `Blood_requests`
--
ALTER TABLE `Blood_requests`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `blood_screening`
--
ALTER TABLE `blood_screening`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `blood_stock`
--
ALTER TABLE `blood_stock`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `campaigns`
--
ALTER TABLE `campaigns`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `donated`
--
ALTER TABLE `donated`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `donor_registration`
--
ALTER TABLE `donor_registration`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `hospital`
--
ALTER TABLE `hospital`
  MODIFY `hospital_id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `hospital_donated`
--
ALTER TABLE `hospital_donated`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `logs`
--
ALTER TABLE `logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `regional_centre`
--
ALTER TABLE `regional_centre`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
