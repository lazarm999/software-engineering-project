insert ignore into api_location(locId, cityName, latitude, longitude) values (1, 'Niš', 43.3209, 21.8954);
insert ignore into api_location(locId, cityName, latitude, longitude) values (2, 'Beograd', 44.8125, 20.4612);
insert ignore into api_location(locId, cityName, latitude, longitude) values (3, 'Kragujevac', 44.0128, 20.9114);
insert ignore into api_location(locId, cityName, latitude, longitude) values (4, 'Subotica', 46.0970, 19.6576);
insert ignore into api_location(locId, cityName, latitude, longitude) values (5, 'Zaječar', 43.9018, 22.2702);
insert ignore into api_location(locId, cityName, latitude, longitude) values (6, 'Novi Sad', 45.2889, 19.7245);
insert ignore into api_location(locId, cityName, latitude, longitude) values (7, 'Leskovac', 42.9963, 21.9430);

insert ignore into api_university(universityId, name) values (1, 'Univerzitet u Nišu');
insert ignore into api_university(universityId, name) values (1, 'Univerzitet u Beogradu');
insert ignore into api_university(universityId, name) values (1, 'Univerzitet u Novom Sadu');

insert ignore into api_faculty(facultyId, university_id, name) values (1, 1, 'Elektronski fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (2, 1, 'Mašinski fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (3, 2, 'Elektrotehnički fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (4, 3, 'Ekonomski fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (5, 3, 'Fakultet tehničkih nauka');
insert ignore into api_faculty(facultyId, university_id, name) values (6, 2, 'Pravni fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (7, 2, 'Mašinski fakultet');
insert ignore into api_faculty(facultyId, university_id, name) values (8, 2, 'Medicinski fakultet');

insert ignore into api_tag(tagId, name) values(1, 'Hostesa');
insert ignore into api_tag(tagId, name) values(2, 'Promoter');
insert ignore into api_tag(tagId, name) values(3, 'Pomoćno osoblje u kuhinji');
insert ignore into api_tag(tagId, name) values(4, 'Anketar');
insert ignore into api_tag(tagId, name) values(5, 'Inkasantski poslovi');
insert ignore into api_tag(tagId, name) values(6, 'Konobar');
insert ignore into api_tag(tagId, name) values(7, 'Lakši fizički poslovi');
insert ignore into api_tag(tagId, name) values(8, 'Teži fizički poslovi');

insert ignore into api_badge(badgeId, description) values(1, 'Dobio je tri ocene preko 4');
insert ignore into api_badge(badgeId, description) values(2, 'U periodu od nedelju dana od registracije prihvaćen je za prvi posao');
insert ignore into api_badge(badgeId, description) values(3, 'Zaradio određenu sumu novca');
insert ignore into api_badge(badgeId, description) values(4, 'Obavio je četiri različite kategorije poslova');
insert ignore into api_badge(badgeId, description) values(5, 'Zaposlio 5 radnika u periodu od 10 dana od registracije');
insert ignore into api_badge(badgeId, description) values(6, 'Dobio je tri ocene 5');
insert ignore into api_badge(badgeId, description) values(7, 'Broj objavljenih oglasa je veci od 15');
insert ignore into api_badge(badgeId, description) values(8, 'Broj prijava na oglasu je 5 puta veci od broja ljudi potrebnih za obavljanje posla');
