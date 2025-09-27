import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'studentFilter',
  standalone: true
})
export class StudentFilterPipe implements PipeTransform {
  transform(students: any[], search: string): any[] {
    if (!students || !search) return students;
    search = search.toLowerCase();
    return students.filter(st =>
      st.studentName.toLowerCase().includes(search) ||
      st.studentEmail.toLowerCase().includes(search) ||
      st.studentId.toString().includes(search) ||
      st.course.toLowerCase().includes(search)
    );
  }
}
