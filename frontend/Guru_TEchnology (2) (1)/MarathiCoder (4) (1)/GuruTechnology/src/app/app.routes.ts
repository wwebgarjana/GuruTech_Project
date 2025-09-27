import { Routes } from '@angular/router';
import { Home } from './home/home';
import { About } from './about/about';
import { Courses } from './courses/courses';
import { Events } from './events/events';
import { Blog } from './blog/blog';
import { Login } from './login/login';
import { ResetPassword } from './reset-password/reset-password';

import { Loader } from './loader/loader';
import { BasicCourses } from './basic-courses/basic-courses';
import { ItCourses } from './it-courses/it-courses';
import { Internship } from './internship/internship';
import { CareerCounselling } from './career-counselling/career-counselling';


import { Dashboard as TrainerDashboard} from './trainer-panel/dashboard/dashboard';
import { Profile as TrainerProfile } from './trainer-panel/profile/profile';
import { Home as TrainerHome } from './trainer-panel/home/home';
import { Assignments as TrainerAssignments } from './trainer-panel/assignments/assignments';
import { Attendance as TrainerAttendance } from './trainer-panel/attendance/attendance';
import { Doubt as TrainerDoubt } from './trainer-panel/doubt/doubt';
import { Feedback as TrainerFeedback } from './trainer-panel/feedback/feedback';
import { Videos as TrainerVideos } from './trainer-panel/videos/videos';
import { Notes as TrainerNotes } from './trainer-panel/notes/notes';
import { Test as TrainerTest } from './trainer-panel/test/test';
import { Schedule as TrainerSchedule } from './trainer-panel/schedule/schedule';
import { MyBatches as TrainerMyBatches} from './trainer-panel/my-batches/my-batches';
import { Project as TrainerProject } from './trainer-panel/project/project';

import { StudentProgressTracker as TrainerStudentProgressTracker } from './trainer-panel/student-progress-tracker/student-progress-tracker';
import { StudyMaterial as TrainerStudyMaterial } from './trainer-panel/study-material/study-material';
import { StudSubmitAssign as TrainerStudSubmitAssign } from './trainer-panel/stud-submit-assign/stud-submit-assign';

import { Dashboard as StudentDashboard } from './student-panel/dashboard/dashboard';
import { Profile as StudentProfile } from './student-panel/profile/profile';
import { Assignments as StudentAssignments } from './student-panel/assignments/assignments';
import { Attendance as StudentAttendance } from './student-panel/attendance/attendance';
import { Doubt as StudentDoubt } from './student-panel/doubt/doubt';
import { Feedback as StudentFeedback } from './student-panel/feedback/feedback';
import { Videos as StudentVideos } from './student-panel/videos/videos';
import { Notes as StudentNotes } from './student-panel/notes/notes';
import { Test as StudentTest } from './student-panel/test/test';
import { LiveClasses as StudentLiveClasses } from './student-panel/live-classes/live-classes';
import { Schedule as StudentSchedule } from './student-panel/schedule/schedule';
import { EnrollCourses as StudentEnrollCourses } from './student-panel/enroll-courses/enroll-courses';
import { Project as StudentProject } from './student-panel/project/project';
import { ApplyJobs as StudentApplyJobs } from './student-panel/apply-jobs/apply-jobs';
import { Home as StudentHome } from './student-panel/home/home';
import { Lessons as StudentLessons } from './student-panel/lessons/lessons';
import { Certification as StudentCertification } from './student-panel/certification/certification';
import { Payment as StudentPayment } from './student-panel/payment/payment';


import { Dashboard as AdminDashboard} from './admin-panel/dashboard/dashboard';
import { Home as AdminHome } from './admin-panel/home/home';
import { Students as AdminStudents } from './admin-panel/students/students';
import { Trainers as AdminTrainers } from './admin-panel/trainers/trainers';
import { CourseManage as AdminCourseManage } from './admin-panel/course-manage/course-manage';
import { Placement as AdminPlacement } from './admin-panel/placement/placement';
import { Feedback as AdminFeedback } from './admin-panel/feedback/feedback';
import { Payment as AdminPayment } from './admin-panel/payment/payment';
import { Accounts as AdminAccounts } from './admin-panel/accounts/accounts';
import { Helps as AdminHelps } from './admin-panel/helps/helps';
import { Certification as AdminCertification } from './admin-panel/certification/certification';
import { Report as AdminReport } from './admin-panel/report/report';
import { AddStudent as AdminAddStudent } from './admin-panel/add-student/add-student';
import { RevokeStudent as AdminRevokeStudent } from './admin-panel/revoke-student/revoke-student';
import { RevokeTrainerComponent as AdminRevokeTrainer } from './admin-panel/revoke-trainer/revoke-trainer';
import { RemoveCourse as AdminRemoveCourse } from './admin-panel/remove-course/remove-course';
import { AddTrainer as AdminAddTrainer } from './admin-panel/add-trainer/add-trainer';
import { JobUpdates } from './trainer-panel/job-updates/job-updates';
import { RevokedUsers } from './admin-panel/revoked-users/revoked-users';





export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'about', component: About },
  { path: 'courses', component: Courses },
  { path: 'events', component: Events },
  { path: 'blog', component: Blog },

  { path: 'loader', component: Loader },
  { path: 'login', component: Login },
  { path: 'reset-password', component: ResetPassword }, 
    { path: 'basic-courses', component: BasicCourses },
  { path: 'career-counselling', component: CareerCounselling },
  { path: 'it-courses', component: ItCourses },
  { path: 'internship', component: Internship },
  { path: '', redirectTo: 'basic-courses', pathMatch: 'full' },

 {
  path: 'trainer-panel/dashboard',
  component: TrainerDashboard,
  children: [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'profile', component: TrainerProfile },
    {path:'home',component:TrainerHome},
    {path:'assignments',component:TrainerAssignments},
    {path:'attendance',component:TrainerAttendance},
    {path:'doubt',component:TrainerDoubt},
    {path:'feedback',component:TrainerFeedback},
    {path:'videos',component:TrainerVideos},
    {path:'notes',component:TrainerNotes},
    {path:'test',component:TrainerTest},
    {path: 'schedule', component: TrainerSchedule },
    {path:'my-batches',component:TrainerMyBatches},
    {path:'project',component:TrainerProject},
    {path:'job-updates',component:JobUpdates},
    {path:'study-material',component:TrainerStudyMaterial},
     {path:'stud-submit-assign',component:TrainerStudSubmitAssign},
    {path:'student-progress-tracker',component:TrainerStudentProgressTracker},
  ]
},


  {
  path: 'student-panel/dashboard',
  component: StudentDashboard,
  children: [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'profile', component: StudentProfile },
    { path: 'home', component: StudentHome },
    { path: 'assignments', component: StudentAssignments },
    { path: 'attendance', component: StudentAttendance },
    { path: 'doubt', component: StudentDoubt },
    { path: 'feedback', component: StudentFeedback },
    { path: 'videos', component: StudentVideos },
    { path: 'notes', component: StudentNotes },
    { path: 'test', component: StudentTest },
    { path: 'schedule', component: StudentSchedule },
    { path: 'enroll-courses', component: StudentEnrollCourses },
    { path: 'project', component: StudentProject },
    { path: 'apply-jobs', component: StudentApplyJobs },
    { path: 'lessons', component: StudentLessons },
    { path: 'live-classes', component: StudentLiveClasses},
    { path: 'certification', component: StudentCertification },
    { path: 'payment', component: StudentPayment }

  ]
},


 {
  path: 'admin-panel/dashboard',
  component: AdminDashboard,
  children: [
    { path: '', redirectTo: 'home', pathMatch: 'full' },
    { path: 'home', component: AdminHome },
    { path: 'students', component: AdminStudents },
    { path: 'trainers', component: AdminTrainers },
    { path: 'course-manage', component: AdminCourseManage },
    { path: 'placement', component: AdminPlacement },
    { path: 'feedback', component: AdminFeedback },
    { path: 'payment', component: AdminPayment },
    { path: 'accounts', component: AdminAccounts },
    { path: 'helps', component: AdminHelps },
    { path: 'certification', component: AdminCertification },
    { path: 'report', component: AdminReport },
    { path: 'add-student', component: AdminAddStudent },
    { path: 'revoke-student', component: AdminRevokeStudent },
    { path: 'add-trainer', component: AdminAddTrainer },
    { path: 'revoke-trainer', component: AdminRevokeTrainer },
    { path: 'remove-course', component: AdminRemoveCourse },
       { path: 'revoked-users', component: RevokedUsers },

  ]
}

];




