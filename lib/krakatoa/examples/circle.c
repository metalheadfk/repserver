/*
 *	The input and output function for the "circle type".
 */

#include <stdio.h>
#include <mi.h>
#include "circle.h"

#ifdef _WIN32
#define DllExport __declspec(dllexport)
#else
#define DllExport
#endif

DllExport
circle_t *
circle_input( mi_lvarchar *text)  
   {
   circle_t *s;
   char *cp;
   int	ret;

   s = (circle_t *) mi_zalloc( sizeof(circle_t) );
   cp = mi_lvarchar_to_string( text );
   sscanf( cp , "%lf %lf %lf", &s->x, &s->y, &s->radius);
   return( s );
   }

DllExport
mi_lvarchar *
circle_output(circle_t *s )
   {
   char buf[512];	
   mi_lvarchar *val;

   sprintf( buf, "x = %lf, y = %lf, radius = %lf", s->x, s->y, s->radius);

   val = mi_new_var( strlen(buf) );
   mi_set_vardata( val, buf );
   return( val );
   }
