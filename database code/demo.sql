set serveroutput on;
exec moviesprocedures.DYNAMICCALL('moviesfunctions.search(''Superman'', ''title'')');

declare
v_array arrID;
v_number number;
v_line varchar(4000);

begin
    dbms_output.put_line('Procedure max:');
    moviesprocedures.max('vote_count');
    dbms_output.put_line('');
    dbms_output.put_line('Procedure top:');
    moviesprocedures.top(3);
    dbms_output.put_line('');
    dbms_output.put_line('Procedure popularFilms:');
    moviesprocedures.popularfilms(3);
    dbms_output.put_line('');
    dbms_output.put_line('Function search:');
    
    v_number := 0;
    v_array := moviesfunctions.search('KZ', 'production_countries');
    dbms_output.put_line(v_array.count);
    for i in 1..v_array.count loop
    dbms_output.put_line(v_array(i));
    end loop;
end;